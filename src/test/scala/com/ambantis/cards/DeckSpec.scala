package com.ambantis.cards



import org.scalatest._

class DeckSpec extends FunSpec with Matchers with BeforeAndAfter {

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
    it("should have a combined value of 380") {
      bjCards.map(_.value).sum should be (380)
    }
  }

  describe("four decks of playing standard playing cards mixed together") {
    it("should have 52*4 stdCards") {
      stdCards4 should have length (4*52)
    }
  }

  describe("A single deck of standard playing stdCards") {
    it("should have 52 stdCards") {
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
  }


}
