package com.ambantis.cards



import org.scalatest._

class DeckSpec extends FunSpec with Matchers with BeforeAndAfter {

  var cards: List[Card] = _
  var cards4: List[Card] = _

  before {
    cards = Deck().shuffle
    cards4 = Deck(4).shuffle
  }

  describe("A single deck of playing cards") {
    it("should have 52 cards") {
      cards should have length (52)
    }
  }

  describe("four decks of playing cards mixed together") {
    it("should have 52*4 cards") {
      cards4 should have length (4*52)
    }
  }

}
