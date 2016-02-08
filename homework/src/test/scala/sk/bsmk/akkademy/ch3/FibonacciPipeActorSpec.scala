package sk.bsmk.akkademy.ch3

import akka.testkit.TestActorRef
import sk.bsmk.akkademy.ch3.messages.{ComputedFibonacciNumber, PipeAskFibonacciNumber, TellFibonacciNumber}

import akka.pattern.ask

import scala.concurrent.Await

import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciPipeActorSpec extends FibonacciSpec {

  describe("A Fibonacci PipeActor") {
    describe("when asked for fibonacci number") {
      it("should return computed fibonacci number") {
        val producerRef = TestActorRef(new FibonacciProducerActor)
        val pipeActorRef = TestActorRef(new FibonacciPipeActor(producerRef))

        val future = pipeActorRef ? PipeAskFibonacciNumber(orderForTest)
        val fibonacciNumber = Await.result(future, 1 second)

        fibonacciNumber should equal(ComputedFibonacciNumber(fibonacciNumbers(orderForTest)))
      }
    }
  }


}
