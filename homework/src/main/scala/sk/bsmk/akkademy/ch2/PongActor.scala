package sk.bsmk.akkademy.ch2

import akka.actor.{Actor, Props, Status}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class PongActor(response: String) extends Actor {

  override def receive: Receive = {
    case "Ping" => sender() ! response
    case _ => sender() ! Status.Failure(new Exception("Unknown message"))
  }

}

object PongActor {

  def props(response: String): Props = {
    Props(classOf[PongActor], response)
  }

}
