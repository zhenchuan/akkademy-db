package sk.bsmk.akkademy.ch3

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import sk.bsmk.akkademy.ch3.messages._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciForwardApiActor(consumer: ActorRef) extends Actor {

  var actualNumber: Option[Long] = None

  override def receive: Receive = LoggingReceive {

    case TellFibonacciNumber(n) => consumer ! ForwardTellFibonacciNumber(n)

    case ComputedFibonacciNumber(n) =>
      actualNumber = Some(n)

  }

}
