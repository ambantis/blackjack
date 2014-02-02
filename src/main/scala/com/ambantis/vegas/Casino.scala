package com.ambantis.vegas

import akka.actor.{PoisonPill, ActorRef, Props, Actor}
import com.ambantis.strategy.BasicStrategy
import com.ambantis.vegas.Dealer.{DealerDone, BeginGame}
import com.ambantis.vegas.Reaper.WatchMe

/**
 * Casino
 * User: Alexandros Bantis
 * Date: 1/30/14
 * Time: 8:29 PM
 */
class Casino(name: String, reaper: ActorRef) extends Actor {
  import Casino._

  val homer = context.actorOf(Player.props("Homer", BasicStrategy, 1000))
  val marge = context.actorOf(Player.props("Marge", BasicStrategy, 1000))
  val lisa = context.actorOf(Player.props("Lisa", BasicStrategy, 1000))
  val bart = context.actorOf(Player.props("Bart", BasicStrategy, 1000))
  val players = List(homer, marge, lisa, bart)

  val dealer = context.actorOf(Dealer.props("Jimmy", 6, 1000, players))


  override def receive: Actor.Receive = {
    case OpenForBusiness =>
      reaper ! WatchMe(self)
      players foreach(reaper ! WatchMe(_))
      reaper ! WatchMe(dealer)
      dealer ! GetToWork

    case DealerDone =>
      self ! PoisonPill
  }
}

object Casino {
  def props(name: String, reaper: ActorRef): Props = Props(classOf[Casino], name, reaper)
  object OpenForBusiness
  object GetToWork

}
