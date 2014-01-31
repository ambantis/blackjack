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
  def move(hand: Hand): PlayAction = if (hand.hardTotal > 16) Stand else Hit
}

trait BasicStrategy {
  import PlayAction._
  private val hardCeiling: Map[Int,Int] = Map(
    2 -> 12, 3 -> 12, 4 -> 11, 5 -> 11, 6 -> 11, 7 -> 16, 8 -> 16, 9 -> 15, 10 -> 14
  )
  private val softCeiling: Map[Int,Int] = Map(
    2 -> 7, 3 -> 8, 4 -> 8, 5 -> 8, 6 -> 8, 7 -> 7, 8 -> 7, 9 -> 8, 10 -> 8
  )

  def move(hand: Hand)(faceUp: Card): PlayAction = {
    if (hand.size == 2 && hand.hasAce) {
      val soft = if (faceUp.isAce) 8 else softCeiling.getOrElse(faceUp.value, 0)
      if (hand.softTotal > soft) Stand else Hit
    } else {
      val hard = if (faceUp.isAce) 15 else hardCeiling.getOrElse(faceUp.value, 0)
      if (hand.hardTotal > hard) Stand else Hit
    }
  }
}




