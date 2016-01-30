package sk.bsmk.akkademy.ch1

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StoreLastStringActor extends Actor {

  var lastReceived = Option.empty[String]
  val log = Logging(context.system, this)

  override def receive: Receive = {

    case s: String =>
      log.info(s"received $s")
      lastReceived = Option(s)

    case o => log.info(s"received unknown message $o")

  }
}
