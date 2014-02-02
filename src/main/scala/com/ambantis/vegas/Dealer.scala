package com.ambantis.vegas

import akka.actor._
import akka.pattern.ask
import com.ambantis.cards.{BlackJackDeck, Deck, Card}
import com.ambantis.vegas.Player.PlayAction._
import scala.collection.mutable
import com.ambantis.hand.Hand
import com.ambantis.vegas.Player.PlayAction.Bet
import com.ambantis.vegas.Casino.GetToWork

/**
 * Dealer
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:33 PM
 */
object Dealer {

  def props(name: String, numDecks: Int, numRounds: Int, players: List[ActorRef]): Props =
    Props(classOf[Dealer], name, numDecks, numRounds, players)

  case object DealerIsReady
  case object DealerDone
  case object YouLost
  case object ShowMeYourCards
  case object BeginGame
  case object BeginRound
  case object FirstRound

  case class YouWon(amt: Int)
  case class Tie(amt: Int)
  case class DealTwo(cards: List[Card], faceUp: Card)
  case class DealOne(card: Card)
}


class Dealer(name: String, numDecks: Int, numRounds: Int, players: List[ActorRef]) extends Actor with Stash {
  import context._
  import Dealer._

  private var rounds: Int = numRounds

  // This represents the players at the table,
  private var seats = mutable.HashMap[ActorRef, (Int, Boolean)]()

  // converts list of players to mutable.HashMap representing seats at the playing table
  for (p <- players) seats += (p -> (0, false))

  private def placeBet(player: ActorRef, amt: Int): Unit = seats update(player, (amt, false))
  private def playerDone(player: ActorRef): Unit = {
    val amt = seats(player)._1
    seats update(player, (amt, true))
  }
  private def leaveTable(player: ActorRef): Unit = { seats remove player }
  private def allBetsIn: Boolean = seats.forall(_._2._1 > 0)
  private def playersDone: Boolean = seats.forall(_._2._2 == true)
  private def scoringDone: Boolean = seats.forall(x => x._2._1 == 0 && !x._2._2)

  private val deck = BlackJackDeck(numDecks)
  // The Shoe is where all the cards are located that haven't been played yet
  private val shoe = scala.collection.mutable.Stack[Card](deck.shuffle: _*)

  private var hand: Hand = _

  private def card: Card = {
    if (shoe.isEmpty) {
      println(s"time for a new deck of cards")
      shoe.pushAll(deck.shuffle)
    }
    shoe.pop()
  }

  def bets: Receive = {
    case Bet(amt) =>
      placeBet(sender, amt)
      if (allBetsIn) {
        become(play)
        self ! FirstRound
      }

    case SoLong =>
      leaveTable(sender)
      if (allBetsIn) {
        become(play)
        self ! FirstRound
      }
  }

  def play: Receive = {
    case FirstRound =>
      val faceUp = card
      hand = Hand(card, faceUp)
      seats.keys foreach { player =>
        player ! DealTwo(List(card, card), faceUp)
      }

    case Hit =>
      sender ! DealOne(card)

    case Score(_, _) =>
      playerDone(sender)
      stash()
      if (playersDone) {
        while(!hand.isBust && hand.hardTotal < 17) hand += card
        println(s"dealer has a $hand")
        unstashAll()
        become(payout)
      }

  }

  def payout: Receive = {
    case Score(playerHand, playerName) =>
      println(s"SCORING: $playerName has a score of ${playerHand.hardTotal} vs. dealer score of ${hand.hardTotal}")
      if (playerHand.isBlackJack)
        sender ! YouWon((seats(sender)._1 * 2.5).toInt)
      if (playerHand.isBust)
        sender ! YouLost
      else if (hand.isBust || hand.hardTotal < playerHand.hardTotal)
        sender ! YouWon(seats(sender)._1 * 2)
      else if (hand.hardTotal == playerHand.hardTotal)
        sender ! Tie(seats(sender)._1)
      else
        sender ! YouLost
      seats update(sender, (0, false))
      if (scoringDone) {
        if (rounds <= 1) {
          println(s"\t$name played $numRounds rounds; this dealer is done.")
          seats.keys foreach(_ ! FinalTally)
          parent ! DealerDone
        } else {
          rounds -= 1
          become(bets)
          seats.keys foreach(_ ! DealerIsReady)
        }
      }
  }

  override def receive: Receive = {
    case GetToWork =>
      become(bets)
      seats.keys foreach(_ ! DealerIsReady)

  }





  // todo:2014-01-31:ambantis:replace behavior so that there are a couple of different `Receive`,
  // there should be one for duringGame, another for when all the players Stand, reverting back to
  // a game after everyone has tallied up
//  override def receive: Receive = {
//    case Bet(amt) =>
//      val dealerBuf = ListBuffer[Card]()
//      val playerBuf = ListBuffer[Card]()
//      playerBuf += card
//      dealerBuf += card
//      playerBuf += card
//      dealerBuf += card
//      hand = Hand(dealerBuf:_*)
//      chairs = chairs - sender + (sender -> amt)
//      sender ! DealTwo(playerBuf.toList, dealerBuf.last)
//
//    case Hit =>
//      sender ! DealOne(card)
//
//    case Stand =>
//      println("all players stand, time for dealer to play his hand")
//      while (!hand.isBust && hand.hardTotal < 17) {
//        hand += card
//        println(s"the dealer has $hand")
//      }
//      sender ! ShowMeYourCards
//
//    case playerHand: Hand =>
//      rounds -= 1
//      if (playerHand.isBlackJack) {
//        println(s"RESULT is blackjack")
//        sender ! YouWon((chairs(sender) * 2.5).toInt)
//      }
//      if (hand.isBust || hand.hardTotal < playerHand.hardTotal) {
//        println(s"RESULT is a win: dealer = $hand, player = $playerHand")
//        sender ! YouWon(chairs(sender) * 2)
//      } else if (hand.hardTotal == playerHand.hardTotal) {
//        println(s"RESULT is a draw: dealer = $hand, player = $playerHand")
//        sender ! YouWon(chairs(sender))
//      } else {
//        println(s"RESULT is a loss: dealer = $hand, player = $playerHand")
//        sender ! YouLost
//      }
//      self ! BeginGame
//
//    case BeginGame =>
//      if (rounds < 1) {
//        context.parent ! PoisonPill
//        context.system.shutdown()
//      } else {
//        println("the dealer is ready")
//        player ! DealerIsReady
//      }
//  }

}

