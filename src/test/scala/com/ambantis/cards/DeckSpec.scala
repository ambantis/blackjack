package com.ambantis.cards



import org.scalatest._

class DeckSpec extends FunSpec with Matchers with BeforeAndAfter {

  var cards: List[Card] = _
  var cards4: List[Card] = _

  before {
    cards = Deck().shuffle
    cards4 = Deck(4).shuffle
  }

  describe("four decks of playing cards mixed together") {
    it("should have 52*4 cards") {
      cards4 should have length (4*52)
    }
  }

  describe("A single deck of playing cards") {
    it("should have 52 cards") {
      cards should have length (52)
    }

    it("should have an equal number of cards in each suit") {
      val cardsInEachSuit: List[Int] = cards.groupBy(_.suit).toList.map(_._2.size)
      cardsInEachSuit.forall(_ == cardsInEachSuit.head) should be (true)
    }

    it("should only have four different suits") {
      cards.groupBy(_.suit) should have size (4)
    }

    it("should have a mean value of 8") {
      cards.map(_.value).sum / cards.size.toDouble should be (8.0)
    }

    it("should have sum of all cards equal to (2 to 14).sum * 4") {
      cards.map(_.value).sum should be ((2 to 14).sum * 4)
    }
  }


}
