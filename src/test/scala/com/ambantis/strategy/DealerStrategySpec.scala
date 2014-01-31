package com.ambantis.strategy

import org.scalatest._
import com.ambantis.cards.{FaceCardBJ, Card, NumberCard}
import com.ambantis.cards.Suit._
import com.ambantis.cards.Face._
import com.ambantis.strategy.PlayAction._

/**
 * StrategySpec
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 3:54 PM
 */
class DealerStrategySpec extends FunSpec with Matchers with BeforeAndAfter with DealerStrategy {

  var lowHand: List[Card] = _
  var highHand: List[Card] = _

  before {
    lowHand = List(NumberCard(3, Diamonds), FaceCardBJ(Jack, Hearts))
    highHand = List(NumberCard(10, Clubs), FaceCardBJ(King, Spades))
  }

  describe("A Dealer Strategy") {
    it("should hit if the hand is less than 17") {
      move(lowHand) should be (Hit)
    }

    it("Should stand if the hand is more than 16") {
      move(highHand) should be (Stand)
    }
  }


}
