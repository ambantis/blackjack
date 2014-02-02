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

  override def receive: Actor.Receive = {

    case DealerIsReady =>
      if (wallet < 0) self ! PoisonPill
      else {
        bet = calcBetAmt
        println(s"$name says, 'I bet $bet'")
        wallet -= bet
        sender ! Bet(bet)
      }

    case DealTwo(cards, dealerCard) =>
      faceUp = dealerCard
      hand = Hand(cards: _*)
      println(s"Humh, dealer has a $faceUp and my hand is $hand")
      if (hand.isBlackJack) sender ! hand
      else sender ! s.move(hand)(faceUp)

    case DealOne(card) =>
      println(s"just received a $card")
      hand = hand + card
      println(s"Humh, dealer has a $faceUp and my hand is $hand")
      if (hand.isBust) sender ! hand
      else sender ! s.move(hand)(faceUp)

    case ShowMeYourCards =>
      sender ! hand

    case YouWon(amt) =>
      wallet += amt
      println(s"Wow, I won, now I have $$$wallet")

    case YouLost =>
      println(s"Sucks, I lost, now I have $$$wallet")
  }

  def calcBetAmt: Int = 25

}

object Player {
  sealed class PlayAction
  object PlayAction {
    case class Blackjack(hand: Hand)
    case object Bust
    case object Play extends PlayAction
    case object Hit extends PlayAction
    case object Stand extends PlayAction
    case object DoubleDown extends PlayAction
    case object Split extends PlayAction
    case object Surrender extends PlayAction
    case class Bet(amt: Int) extends PlayAction
  }

  def props(name: String,
            strategy: PlayerStrategy,
            cash: Int): Props = Props(classOf[Player], name, strategy, cash)
}
