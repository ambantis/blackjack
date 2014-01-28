package com.ambantis.cards

sealed abstract class Suit
object Suit {
  case object Diamonds extends Suit
  case object Hearts extends Suit
  case object Clubs extends Suit
  case object Spades extends Suit
  val suits = List(Diamonds, Hearts, Clubs, Spades)
}

sealed abstract class Face
object Face {
  case object Jack extends Face { val value = 11; override def toString = "Jack" }
  case object Queen extends Face { val value = 12; override def toString = "Queen" }
  case object King extends Face { val value = 13; override def toString = "King" }
  case object Ace extends Face { val value = 14; override def toString = "Ace" }
  val faces = List(Jack, Queen, King, Ace)
}

sealed abstract class Card extends Ordered[Card] {
  def value: Int
  def suit: Suit
  def compare(that: Card): Int = this.value - that.value
  def equals(that: Card): Boolean = compare(that) == 0
  override def hashCode = 42 * value.hashCode
  override def toString = s"$value of $suit"
}

case class NumberCard(value: Int, suit: Suit) extends Card

case class FaceCard(face: Face, suit: Suit) extends Card {
  import Face._
  def value = face match {
    case Jack  => 11
    case Queen => 12
    case King  => 13
    case Ace   => 14
  }
  override def toString = s"$face of $suit"
}

trait DeckBuilder {
  import Face._
  import Suit._

  val numberCards =
    for {
      n <- (2 to 10).toList
      s <- suits
    } yield NumberCard(n, s)

  val faceCards =
    for {
      f: Face <- faces
      s <- suits
    } yield FaceCard(f, s)
}

abstract class Deck {
  def shuffle: List[Card]
}

object Deck {
  def apply(): Deck = new Deck with DeckBuilder {
    private def cards = numberCards ++ faceCards
    def shuffle: List[Card] = cards
  }
}
