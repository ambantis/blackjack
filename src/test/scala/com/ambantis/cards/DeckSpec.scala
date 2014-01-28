package com.ambantis.cards



import org.scalatest._

class DeckSpec extends FunSpec with Matchers with BeforeAndAfter {

  var cards: List[Card] = _

  before {
    cards = Deck().shuffle
  }

  describe("A single deck of playing cards") {
    it("should have 52 cards") {
      cards should have length (52)
    }
  }

}
