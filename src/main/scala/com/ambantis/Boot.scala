package com.ambantis

import akka.actor.{Props, ActorSystem}
import com.ambantis.vegas.{Reaper, Casino}
import com.ambantis.vegas.Casino.LetsPlay

/**
 * Boot
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 7:54 PM
 */
object Boot extends App {

  implicit val system = ActorSystem("BlackJack")

  val reaper = system.actorOf(Reaper.props)
  val casino = system.actorOf(Casino.props("Sands", reaper))


  casino ! LetsPlay
}
