package com.ambantis.hand

import com.ambantis.cards.Card

/**
 * Hand
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 5:13 PM
 */
case class Hand(cards: Card*) {
  private val _cards = List(cards: _*)
  def size: Int = _cards.length
  def hasAce: Boolean = _cards.exists(_.isAce)
  def softTotal: Int = _cards.map(_.value).sum
  def hardTotal: Int = {
    var numAces: Int = _cards.count(_.isAce)
    var result = _cards.map(_.value).sum
    while (numAces > 0 && result + 10 < 22) {
      numAces -= 1
      result += 10
    }
    result
  }
  def isBust: Boolean = hardTotal > 21
}
