package sk.bsmk.akkademy

import akka.actor.Actor
import akka.event.Logging
import sk.bsmk.akkademy.messages.SetRequest

import scala.collection.mutable

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDb extends Actor {

  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) =>
      log.info(s"received SetRequest - key: $key value: $value")
      map.put(key, value)

    case o => log.info(s"received unknown message $o")
  }

}
