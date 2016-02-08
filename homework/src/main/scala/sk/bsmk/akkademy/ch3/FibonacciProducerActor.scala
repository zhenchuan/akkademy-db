package sk.bsmk.akkademy.ch3

import akka.actor.Actor
import akka.event.LoggingReceive
import sk.bsmk.akkademy.ch3.messages.{TellFibonacciNumber, ComputedFibonacciNumber, AskFibonacciNumber}

import scala.collection.mutable

/**
  * Created by miroslav.matejovsky on 01/02/16.
  */
class FibonacciProducerActor extends Actor {

  /** fibonacci numbers stored in map
    * 1 -> 1
    * 2 -> 1
    * 3 -> 2
    * 4 -> 3
    * 5 -> 5
    * 6 -> 8
    */
  val numbers = mutable.Map[Int, Long]((1, 1), (2, 1))

  override def receive: Receive = LoggingReceive {

    case AskFibonacciNumber(n) =>
      val value = compute(n)
      sender() ! ComputedFibonacciNumber(value)

    case TellFibonacciNumber(n) =>
      val value = compute(n)
      sender() ! ComputedFibonacciNumber(value)

  }

  private def compute(order: Int): Long = {
    numbers.getOrElseUpdate(order, compute(order - 1) + compute(order - 2))
  }

}
