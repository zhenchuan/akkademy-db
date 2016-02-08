package sk.bsmk.akkademy.ch3

import akka.testkit.TestActorRef
import sk.bsmk.akkademy.ch3.messages.TellFibonacciNumber

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciConsumerActorSpec extends FibonacciSpec {

  describe("when asked for fibonacci number") {
    it("should return one") {
      val producerRef = TestActorRef(new FibonacciProducerActor)
      val consumerRef = TestActorRef(new FibonacciConsumerActor(producerRef))

      consumerRef.underlyingActor.actualNumber should equal(Option.empty)

      consumerRef ! TellFibonacciNumber(orderForTest)

      // KILL ME!!
      defaultSleep()

      consumerRef.underlyingActor.actualNumber should equal(Some(fibonacciNumbers(orderForTest)))
    }
  }

}
