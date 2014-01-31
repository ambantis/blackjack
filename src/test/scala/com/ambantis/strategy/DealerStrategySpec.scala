package com.ambantis.strategy

import com.ambantis.cards.{BlackJackDeck, Card}
import com.ambantis.strategy.PlayAction._

import org.scalatest._
import Inspectors._

/**
 * StrategySpec
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 3:54 PM
 */
class DealerStrategySpec extends FunSpec with Matchers with DealerStrategy {

  describe("A Dealer Strategy") {
    it("should hit if the hand is less than 17") {
      val lowHands: List[List[Card]] =
        for {
          a <- BlackJackDeck().shuffle
          b <- BlackJackDeck().shuffle
          if a.value + b.value < 17
        } yield a :: b :: Nil
      forAll (lowHands) { hand => move(hand) should be (Hit) }
    }

    it("Should stand if the hand is more than 16") {
      val highHands: List[List[Card]] =
        for {
          a <- BlackJackDeck().shuffle
          b <- BlackJackDeck().shuffle
          if a.value + b.value > 16
        } yield a :: b :: Nil
      forAll(highHands) { hand => move(hand) should be (Stand) }
    }
  }


}
