package com.ambantis.cards

sealed abstract class Suit
object Suit {
  case object Diamonds extends Suit
  case object Hearts extends Suit
  case object Clubs extends Suit
  case object Spades extends Suit
  val suits = List(Diamonds, Hearts, Clubs, Spades)
}

sealed abstract class Face {
  def value: Int
}

object Face {
  case object Jack extends Face { val value = 11; override def toString = "Jack" }
  case object Queen extends Face { val value = 12; override def toString = "Queen" }
  case object King extends Face { val value = 13; override def toString = "King" }
  case object Ace extends Face { val value = 14; override def toString = "Ace" }
  val faces = List(Jack, Queen, King, Ace)
}

sealed abstract class Card extends Ordered[Card] {
  import Face._
  def value: Int
  def suit: Suit
  def isAce: Boolean = this match {
    case _ : NumberCard   => false
    case fc: FaceCard => fc.face match {
      case Ace => true
      case _   => false
    }
  }
  def compare(that: Card): Int = this.value - that.value
  def equals(that: Card): Boolean = compare(that) == 0
  override def hashCode = 42 * value.hashCode
  override def toString = s"$value of $suit"
}

case class NumberCard(value: Int, suit: Suit) extends Card

abstract class FaceCard extends Card {
  def face: Face
  def suit: Suit
  def value: Int
}

case class FaceCardStd(face: Face, suit: Suit) extends FaceCard {
  def value = face.value
  override def toString = s"$face of $suit"
}

case class FaceCardBJ(face: Face, suit: Suit) extends FaceCard {
  import Face._
  override def value = face match {
    case Ace => 1
    case _   => 10
  }
}

trait DeckBuilder {
  import Face._
  import Suit._

  val numberCards =
    for {
      n <- (2 to 10).toList
      s <- suits
    } yield NumberCard(n, s)

  val faceCardsStd: List[FaceCard] =
    for {
      f <- faces
      s <- suits
    } yield FaceCardStd(f, s)

  val faceCardsBJ: List[FaceCard] =
    for {
      f <- faces
      s <- suits
    } yield FaceCardBJ(f, s)
}

abstract class Deck {
  def shuffle: List[Card]
}

trait Shuffler {
  def shuffleCards(cards: List[Card]): List[Card] =
    cards.zipWithIndex.map(x => (x._1, math.random)).sortBy(_._2).map(_._1)
}

object Deck {
  def apply(): Deck = new Deck with DeckBuilder {
    private def cards = numberCards ++ faceCardsStd
    def shuffle: List[Card] = cards
  }

  def apply(n: Int): Deck = new Deck with DeckBuilder {
    private def cards = (1 to n).toList.flatMap(x => numberCards ++ faceCardsStd)
    def shuffle: List[Card] = cards
  }
}

object BlackJackDeck {
  def apply(): Deck = new Deck with DeckBuilder with Shuffler {
    private def cards = numberCards ++ faceCardsBJ
    def shuffle: List[Card] = shuffleCards(cards)
  }

  def apply(n: Int): Deck = new Deck with DeckBuilder with Shuffler {
    private def cards = (1 to n).toList.flatMap(x => numberCards ++ faceCardsBJ)
    def shuffle: List[Card] = shuffleCards(cards)
  }

}
