package com.ambantis.hand

import com.ambantis.cards.Card

/**
 * Hand
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 5:13 PM
 */
case class Hand(card: Card*) {
  val _cards = List(card: _*)
  def points: Int = {
    var numAces: Int = _cards.count(_.isAce)
    var result = _cards.map(_.value).sum
    while (numAces > 0 && result + 10 < 22) {
      numAces -= 1
      result += 10
    }
    result
  }
  def isBust: Boolean = points > 21
}
