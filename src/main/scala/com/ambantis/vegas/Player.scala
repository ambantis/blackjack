package com.ambantis.vegas

import akka.actor.{PoisonPill, Props, Actor}
import com.ambantis.strategy.PlayerStrategy
import com.ambantis.hand.Hand
import com.ambantis.cards.Card

/**
 * Player
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:35 PM
 */
class Player(name: String, s: PlayerStrategy, cash: Int) extends Actor {
  import Player.PlayAction._
  import Dealer._

  var wallet: Int = cash
  var hand: Hand = _
  var faceUp: Card = _
  var bet: Int = _

  override def receive: Receive = {

    case DealerIsReady =>
      bet = calcBetAmt
      if (wallet < bet) {
        println(s"$name is am out of money!, so long")
        sender ! SoLong
        self ! PoisonPill
      } else {
        println(s"$name says, 'I bet $bet'")
        wallet -= bet
        sender ! Bet(bet)
      }

    case DealTwo(cards, dealerCard) =>
      faceUp = dealerCard
      hand = Hand(cards: _*)
      println(s"Humh, dealer has a $faceUp and $name's hand is $hand")
      if (hand.isBlackJack) sender ! Score(hand, name)
      else sender ! s.move(hand, name)(faceUp)

    case DealOne(card) =>
      println(s"$name just received a $card")
      hand = hand + card
      println(s"Humh, dealer has a $faceUp and $name's is $hand")
      if (hand.isBust) sender ! Score(hand, name)
      else sender ! s.move(hand, name)(faceUp)

    case ShowMeYourCards =>
      sender ! hand

    case YouWon(amt) =>
      wallet += amt
      println(s"Wow, $name won, now I have $$$wallet")

    case Tie(amt) =>
      wallet += amt
      println(s"Oh well, $name tied and now has $$$wallet")

    case YouLost =>
      println(s"Sucks, $name lost, now I have $$$wallet")

    case FinalTally =>
      println(s"\t$name ended with $wallet")
  }

  def calcBetAmt: Int = 25

}

object Player {
  sealed class PlayAction
  object PlayAction {
    case object Play extends PlayAction
    case object Hit extends PlayAction
    case object Stand extends PlayAction
    case object DoubleDown extends PlayAction
    case object Split extends PlayAction
    case object Surrender extends PlayAction
    case class Score(hand: Hand, name: String) extends PlayAction
    case object SoLong
    case class Bet(amt: Int) extends PlayAction
    case object FinalTally
  }

  def props(name: String,
            strategy: PlayerStrategy,
            cash: Int): Props = Props(classOf[Player], name, strategy, cash)
}
