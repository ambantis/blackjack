package com.ambantis.strategy

import com.ambantis.cards._
import com.ambantis.hand.Hand

sealed abstract class PlayAction

object PlayAction {
  case object Hit extends PlayAction
  case object Stand extends PlayAction
  case object DoubleDown extends PlayAction
  case object Split extends PlayAction
  case object Surrender extends PlayAction
  val playActions = List(Hit,Stand,DoubleDown, Split, Surrender)
}

trait DealerStrategy {
  import PlayAction._
  def move(hand: Hand): PlayAction = if (hand.points > 16) Stand else Hit
}

trait BasicStrategy {
  import PlayAction._
  private val calcHitCeiling: Map[Int,Int] = Map(
    2 -> 12, 3 -> 12, 4 -> 11, 5 -> 11, 6 -> 11, 7 -> 16, 8 -> 16, 9 -> 15, 10 -> 14
  )
  def move(hand: Hand)(faceUp: Card): PlayAction = {
    val ceiling = if (faceUp.isAce) 15 else calcHitCeiling.getOrElse(faceUp.value, 0)
    if (hand.points > ceiling) Stand else Hit
  }
}




