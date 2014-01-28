package com.ambantis.cards

sealed abstract class Suit
object Suit {
  case object Diamonds
  case object Hearts
  case object Clubs
  case object Spades
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

abstract class Deck {
  def shuffle: List[Card]
}

object Deck {
  def apply(): Deck = new Deck {
    def shuffle: List[Card] = List()
  }
}
