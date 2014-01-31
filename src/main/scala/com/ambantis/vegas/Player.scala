package com.ambantis.vegas

import akka.actor.{Props, Actor}

/**
 * Player
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:35 PM
 */
class Player(name: String) extends Actor {
  override def receive: Actor.Receive = ???
}

object Player {
  def props(name: String): Props = Props(classOf[Player], name)
}
