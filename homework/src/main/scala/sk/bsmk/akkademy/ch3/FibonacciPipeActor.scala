package sk.bsmk.akkademy.ch3

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import akka.util.Timeout
import sk.bsmk.akkademy.ch3.messages.{AskFibonacciNumber, PipeAskFibonacciNumber}
import akka.pattern.ask
import akka.pattern.pipe
import scala.concurrent.Future
import scala.concurrent.duration._
import language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciPipeActor(producer: ActorRef) extends Actor {

  implicit val timeout = Timeout(5 seconds)

  override def receive: Receive = LoggingReceive {

    case PipeAskFibonacciNumber(n) =>
      val future: Future[Any] = producer ? AskFibonacciNumber(n)
      future pipeTo sender()
      //pipe(future) to sender()
  }

}
