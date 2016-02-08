package sk.bsmk.akkademy.ch3

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import akka.util.Timeout
import sk.bsmk.akkademy.ch3.messages._
import akka.pattern.ask
import akka.pattern.pipe
import scala.concurrent.duration._
import language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciPublicApiActor(consumer: ActorRef) extends Actor {

  implicit val timeout = Timeout(5 seconds)

  var actualNumber: Option[Long] = None

  override def receive: Receive = LoggingReceive {

    case TellFibonacciNumber(n) => consumer ! ForwardTellFibonacciNumber(n)

    case PipeAsk(n) =>
      val future = consumer ? AskFibonacciNumber(n)
      pipe(future) to sender()


    case ComputedFibonacciNumber(n) =>
      actualNumber = Some(n)

  }

}
