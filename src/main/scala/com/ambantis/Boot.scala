package com.ambantis

import akka.actor.{Props, ActorSystem}

/**
 * Boot
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 7:54 PM
 */
class Boot extends App {

  implicit val system = ActorSystem("BlackJack")

}
