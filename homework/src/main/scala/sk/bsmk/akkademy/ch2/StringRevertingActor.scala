package sk.bsmk.akkademy.ch2

import akka.actor.{Status, Actor}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StringRevertingActor extends Actor {

  override def receive: Receive = {
    case s: String => sender() ! s.reverse
    case o         => sender() ! Status.Failure
  }

}
