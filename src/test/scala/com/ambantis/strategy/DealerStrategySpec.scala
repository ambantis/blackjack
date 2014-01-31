package com.ambantis.strategy

import com.ambantis.cards.BlackJackDeck
import com.ambantis.strategy.PlayAction._

import org.scalatest._
import org.scalatest.Inspectors._
import com.ambantis.hand.Hand

/**
 * StrategySpec
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 3:54 PM
 */
class DealerStrategySpec extends FunSpec with Matchers with DealerStrategy {

  describe("A Dealer Strategy") {
    it("should hit if the hand is less than 17") {
      val lowHands: List[Hand] =
        for {
          a <- BlackJackDeck().shuffle
          b <- BlackJackDeck().shuffle
          hand = Hand(a,b) if hand.hardTotal < 17
        } yield hand
      forAll (lowHands) { hand => move(hand) should be (Hit) }
    }

    it("Should stand if the hand is more than 16") {
      val highHands: List[Hand] =
        for {
          a <- BlackJackDeck().shuffle
          b <- BlackJackDeck().shuffle
          hand = Hand(a,b) if hand.hardTotal > 16
        } yield hand
      forAll(highHands) { hand => move(hand) should be (Stand) }
    }
  }


}
