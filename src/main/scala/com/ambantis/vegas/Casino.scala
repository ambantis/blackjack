package com.ambantis.vegas

import akka.actor.{Props, Actor}

/**
 * Casino
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:29 PM
 */
class Casino(name: String) extends Actor {

  override def receive: Actor.Receive = ???
}

object Casino {
  def props(name: String): Props = Props(classOf[Casino], name)
}
