package com.ambantis.hand

import com.ambantis.cards.Card
import scala.collection.mutable.ListBuffer

/**
 * Hand
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 5:13 PM
 */
case class Hand(cards: Card*) {
  private val hand = ListBuffer(cards: _*)
  def +(cards: Card*): Hand = { Hand(hand ++ cards:_*) }
  def size: Int = hand.length
  def hasAce: Boolean = hand.exists(_.isAce)
  def softTotal: Int = hand.map(_.value).sum
  def hardTotal: Int = {
    var numAces: Int = hand.count(_.isAce)
    var result = hand.map(_.value).sum
    while (numAces > 0 && result + 10 < 22) {
      numAces -= 1
      result += 10
    }
    result
  }
  def isBust: Boolean = hardTotal > 21
  def isBlackJack: Boolean = size == 2 & hasAce && hardTotal == 21
  override def toString = cards.sortBy(_.value).toString()
}
