package com.ambantis

import akka.actor.ActorSystem
import com.ambantis.vegas.{Casino, ProductionReaper}
import com.ambantis.vegas.Casino.OpenForBusiness

/**
 * Boot
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 7:54 PM
 */
object Boot extends App {

  implicit val system = ActorSystem("Blackjack")

  val reaper = system.actorOf(ProductionReaper.props("grim"))
  val casino = system.actorOf(Casino.props("Sands", reaper))


  casino ! OpenForBusiness
}
