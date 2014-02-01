package com.ambantis.vegas

import akka.actor.{Props, Actor}
import com.ambantis.strategy.BasicStrategy
import com.ambantis.vegas.Casino.LetsPlay
import com.ambantis.vegas.Dealer.BeginGame

/**
 * Casino
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:29 PM
 */
class Casino(name: String) extends Actor {

  val bart = context.actorOf(Player.props("Bart", BasicStrategy, 1000))
  val dealer = context.actorOf(Dealer.props("Jimmy", 1, 10, bart))


  override def receive: Actor.Receive = {
    case LetsPlay =>
      dealer ! BeginGame
  }
}

object Casino {
  def props(name: String): Props = Props(classOf[Casino], name)
  object LetsPlay
}
