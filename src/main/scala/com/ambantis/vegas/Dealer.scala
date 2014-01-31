package com.ambantis.vegas

import akka.actor.{Props, Actor}

/**
 * Dealer
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:33 PM
 */
class Dealer(name: String) extends Actor {
  override def receive: Actor.Receive = ???
}

object Dealer {
  def props(name: String): Props = Props(classOf[Dealer], name)
}
