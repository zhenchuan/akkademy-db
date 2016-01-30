package sk.bsmk.akkademy.ch1

import akka.actor.{ActorLogging, Actor}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StoreLastStringActor extends Actor with ActorLogging {

  var lastReceived = Option.empty[String]

  override def receive: Receive = {

    case s: String =>
      log.info(s"received $s")
      lastReceived = Option(s)

    case o => log.info(s"received unknown message $o")

  }
}
