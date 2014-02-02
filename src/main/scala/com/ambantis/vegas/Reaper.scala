package com.ambantis.vegas

import akka.actor.{Props, Terminated, Actor, ActorRef}
import scala.collection.mutable.ArrayBuffer

/**
 * Reaper
 * User: Alexandros Bantis
 * Date: 2/1/14
 * Time: 4:27 PM
 */
object Reaper {
  // Used by others to register an Actor for watching
  case class WatchMe(ref: ActorRef)
  def props: Props = Props(classOf[Reaper])
}

abstract class Reaper extends Actor {
  import Reaper._

  // Keep track of what we're watching
  val watched = ArrayBuffer.empty[ActorRef]

  // Derivations need to implement this method. It's the hood that's called when everything's dead
  def allSoulsReaped(): Unit

  // Watch and check for termination
  final def receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      watched += ref
    case Terminated(ref) =>
      watched -= ref
      if (watched.isEmpty) allSoulsReaped()
  }
}

class ProductionReaper(name: String) extends Reaper {
  // shutdown
  def allSoulsReaped(): Unit = context.system.shutdown()
}

object ProductionReaper {
  def props(name: String): Props = Props(classOf[ProductionReaper], name)
}
