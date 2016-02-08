package sk.bsmk.akkademy.ch3

import akka.testkit.TestActorRef
import sk.bsmk.akkademy.ch3.messages.TellFibonacciNumber

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciForwardActorSpec extends FibonacciSpec {

  describe("A Fibonacci ForwardActor") {
    describe("when asked for fibonacci number") {
      it("should return computed fibonacci number") {
        val producerRef = TestActorRef(new FibonacciProducerActor)
        val consumerRef = TestActorRef(new FibonacciConsumerActor(producerRef))
        val publicApiRef = TestActorRef(new FibonacciForwardActor(consumerRef))

        consumerRef.underlyingActor.actualNumber should equal(Option.empty)
        publicApiRef.underlyingActor.actualNumber should equal(Option.empty)

        publicApiRef ! TellFibonacciNumber(orderForTest)

        // KILL ME!!
        defaultSleep()

        consumerRef.underlyingActor.actualNumber should equal(Option.empty)
        publicApiRef.underlyingActor.actualNumber should equal(Some(fibonacciNumbers(orderForTest)))
      }
    }
  }

}
