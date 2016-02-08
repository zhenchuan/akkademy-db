package sk.bsmk.akkademy.ch3

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import sk.bsmk.akkademy.ch3.messages.{ComputedFibonacciNumber, AskFibonacciNumber}
import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps
import akka.pattern.ask

/**
  * Created by miroslav.matejovsky on 07/02/16.
  */
class FibonacciProducerActorSpec extends FibonacciSpec{

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("A Fibonacci producer") {
    describe("when asked for fibonacci number") {
      it("should return computed fibonacci number") {
        val actorRef = TestActorRef(new FibonacciProducerActor)

        val fibonacciNumberPromise = actorRef ? AskFibonacciNumber(orderForTest)
        val fibonacciNumber = Await.result(fibonacciNumberPromise, 1 second)

        fibonacciNumber should equal(ComputedFibonacciNumber(fibonacciNumbers(orderForTest)))
        actorRef.underlyingActor.numbers.size should equal(orderForTest)
      }
    }
  }

}
