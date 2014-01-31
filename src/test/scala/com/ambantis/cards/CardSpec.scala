package com.ambantis.cards

import org.scalatest._

class CardSpec extends FunSpec with Matchers with BeforeAndAfter {

  var stdCards: List[Card] = _
  var stdCards4: List[Card] = _
  var bjCards: List[Card] = _
  var bjCards4: List[Card] = _

  before {
    stdCards = Deck().shuffle
    stdCards4 = Deck(4).shuffle
    bjCards = BlackJackDeck().shuffle
    bjCards4 = BlackJackDeck(4).shuffle
  }

  describe("a blackjack deck") {
    it("should have a combined value of 340 (aces = 1)") {
      bjCards.map(_.value).sum should be (340)
    }
  }

  describe("four decks of playing standard playing cards mixed together") {
    it("should have 52*4=208 cards") {
      stdCards4 should have length (4*52)
    }
  }

  describe("A single deck of standard playing cards") {
    it("should have 52 cards") {
      stdCards should have length (52)
    }

    it("should have an equal number of cards in each suit") {
      val stdCardsInEachSuit: List[Int] = stdCards.groupBy(_.suit).toList.map(_._2.size)
      stdCardsInEachSuit.forall(_ == stdCardsInEachSuit.head) should be (true)
    }

    it("should only have four different suits") {
      stdCards.groupBy(_.suit) should have size (4)
    }

    it("should have a mean value of 8") {
      stdCards.map(_.value).sum / stdCards.size.toDouble should be (8.0)
    }

    it("should have sum of all cards equal to (2 to 14).sum * 4") {
      stdCards.map(_.value).sum should be ((2 to 14).sum * 4)
    }

    it("should have cards in a random order") {
      def calcDiffBiggersSmallers(n: Int): Int = {
        def iter(biggers: Int, smallers: Int, rem: List[Card]): Int = rem match {
          case (c :: Nil)       => biggers - smallers
          case (c1 :: c2 :: cs) if c1 > c2 => iter(biggers + 1, smallers, rem.tail)
          case _                           => iter(biggers, smallers + 1, rem.tail)
        }
        iter(0, 0, Deck(n).shuffle)
      }
      val result = calcDiffBiggersSmallers(1000).toDouble

      result should be < (1000 * 52 * 0.01)
    }
  }
}
