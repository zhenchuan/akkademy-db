package sk.bsmk.akkademy.ch3

import akka.actor.{ActorLogging, ActorRef, Actor}
import akka.event.LoggingReceive
import sk.bsmk.akkademy.ch3.messages.{ComputedFibonacciNumber, TellFibonacciNumber}

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciConsumerActor(producer: ActorRef) extends Actor {

  var actualNumber = Option.empty[Long]

  override def receive: Receive = LoggingReceive {

    case TellFibonacciNumber(n) =>
      producer ! TellFibonacciNumber(n)

    case ComputedFibonacciNumber(number) =>
      actualNumber = Option(number)

  }

}
