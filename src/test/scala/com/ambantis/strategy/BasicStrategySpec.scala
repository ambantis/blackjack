package com.ambantis.strategy

import com.ambantis.cards.{BlackJackDeck, NumberCard, FaceCardBJ, Card}
import com.ambantis.cards.Suit._
import com.ambantis.cards.Face._
import com.ambantis.hand.Hand
import com.ambantis.strategy.PlayAction._

import org.scalatest._
import org.scalatest.Inspectors._

/**
 * BasicStrategySpec
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 4:40 PM
 */
class BasicStrategySpec extends FunSpec with BeforeAndAfter with Matchers {

  var ace: Card = _
  var two: Card = _
  var three: Card = _
  var four: Card = _
  var five: Card = _
  var six: Card = _
  var seven: Card = _
  var eight: Card = _
  var nine: Card = _
  var ten: Card = _
  var handCombos: List[Hand] = _
  var strategy: PlayerStrategy = _


  before {
    ace =   FaceCardBJ(Ace, Diamonds)
    two =   NumberCard(2, Hearts)
    three = NumberCard(3, Clubs)
    four =  NumberCard(4, Spades)
    five =  NumberCard(5, Diamonds)
    six =   NumberCard(6, Hearts)
    seven = NumberCard(7, Clubs)
    eight = NumberCard(8, Spades)
    nine =  NumberCard(9, Diamonds)
    ten =   NumberCard(10, Hearts)

    handCombos =
      for {
        a <- BlackJackDeck().shuffle
        b <- BlackJackDeck().shuffle
      } yield Hand(a,b)

    strategy = new BasicStrategy

  }

  describe("A Play Basic Strategy") {
    describe("for a 2-card hand with one ace") {
      it("should hit with a soft total under 8 if dealer has a 2, 7-8") {
        val hands = handCombos.filter(x => x.hasAce && x.softTotal < 8)
        val faceUps = List(two, seven, eight)
        forAll(hands) { hand =>
          forAll(faceUps) { faceUp =>
            strategy.move(hand)(faceUp) should be (Hit) }
        }
      }
      it("should stand with a soft total over 7 if dealer has a 2, 7-8") {
        val hands = handCombos.filter(x => x.hasAce && x.softTotal > 7)
        val faceUps = List(two, seven, eight)
        forAll(hands) { hand =>
          forAll(faceUps) { faceUp =>
            strategy.move(hand)(faceUp) should be (Stand) }
        }
      }
      it("should hit for a soft total under 9 if dealer has 3-6, 8-10,Ace") {
        val hands = handCombos.filter(x => x.hasAce && x.softTotal < 9)
        val faceUps = List(three, four, nine, ten, ace)
        forAll(hands) { hand =>
          forAll(faceUps) { faceUp =>
            strategy.move(hand)(faceUp) should be (Hit)
          }
        }
      }
      it("should stand for a soft total over 8 if dealer has 3-6, 8-10,Ace") {
        val hands = handCombos.filter(x => x.hasAce && x.softTotal > 8)
        val faceUps = List(three, four, nine, ten, ace)
        forAll(hands) { hand =>
          forAll(faceUps) { faceUp =>
            strategy.move(hand)(faceUp) should be (Stand)
          }
        }
      }







    }
  }



}
