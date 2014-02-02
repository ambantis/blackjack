package com.ambantis.vegas

import akka.actor._
import akka.pattern.ask
import com.ambantis.cards.{BlackJackDeck, Deck, Card}
import scala.collection.mutable.ListBuffer
import com.ambantis.hand.Hand
import com.ambantis.vegas.Player.PlayAction.{Blackjack, Stand, Hit, Bet}

/**
 * Dealer
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:33 PM
 */
class Dealer(name: String, numDecks: Int, numRounds: Int, player: ActorRef) extends Actor {
  import Dealer._

  private val deck = BlackJackDeck(numDecks)

  private var hand = Hand()
  private var chairs = Map[ActorRef, Int]()
  chairs = chairs - player + (player -> 0)

  // The Shoe is where all the cards are located that haven't been played yet
  private val shoe = scala.collection.mutable.Stack[Card](deck.shuffle: _*)
  private var rounds: Int = numRounds

  // todo:2014-01-31:ambantis:replace behavior so that there are a couple of different `Receive`,
  // there should be one for duringGame, another for when all the players Stand, reverting back to
  // a game after everyone has tallied up
  override def receive: Actor.Receive = {

    case Bet(amt) =>
      val dealerBuf = ListBuffer[Card]()
      val playerBuf = ListBuffer[Card]()
      playerBuf += card
      dealerBuf += card
      playerBuf += card
      dealerBuf += card
      hand = Hand(dealerBuf:_*)
      chairs = chairs - sender + (sender -> amt)
      sender ! DealTwo(playerBuf.toList, dealerBuf.last)

    case Hit =>
      sender ! DealOne(card)

    case Stand =>
      println("all players stand, time for dealer to play his hand")
      while (!hand.isBust && hand.hardTotal < 17) {
        hand += card
        println(s"the dealer has $hand")
      }
      sender ! ShowMeYourCards

    case playerHand: Hand =>
      rounds -= 1
      if (playerHand.isBlackJack) {
        println(s"RESULT is blackjack")
        sender ! YouWon((chairs(sender) * 2.5).toInt)
      }
      if (hand.isBust || hand.hardTotal < playerHand.hardTotal) {
        println(s"RESULT is a win: dealer = $hand, player = $playerHand")
        sender ! YouWon(chairs(sender) * 2)
      } else if (hand.hardTotal == playerHand.hardTotal) {
        println(s"RESULT is a draw: dealer = $hand, player = $playerHand")
        sender ! YouWon(chairs(sender))
      } else {
        println(s"RESULT is a loss: dealer = $hand, player = $playerHand")
        sender ! YouLost
      }
      self ! BeginGame

    case BeginGame =>
      if (rounds < 1) {
        context.parent ! PoisonPill
        context.system.shutdown()
      } else {
        println("the dealer is ready")
        player ! DealerIsReady
      }
  }

  private def card: Card = {
    if (shoe.isEmpty) {
      println(s"time for a new deck of cards")
      shoe.pushAll(deck.shuffle)
    }
    shoe.pop()
  }
}

object Dealer {

  case class PlaceBet(amt: Int)
  case object DealerIsReady
  case object DealerDone
  case class YouWon(amt: Int)
  case object YouLost

  case object ShowMeYourCards

  case object BeginGame
  case object BeginRound

  case class DealTwo(cards: List[Card], faceUp: Card)
  case class DealOne(card: Card)

  def props(name: String, numDecks: Int, numRounds: Int, player: ActorRef): Props =
    Props(classOf[Dealer], name, numDecks, numRounds, player)
}
