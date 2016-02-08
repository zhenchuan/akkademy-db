package sk.bsmk.akkademy.ch3

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import sk.bsmk.akkademy.ch3.messages.{TellFibonacciNumber, ComputedFibonacciNumber, AskFibonacciNumber}

import scala.concurrent.Await

import scala.concurrent.duration._
import language.postfixOps
import akka.pattern.ask

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciPublicApiActorSpec extends FibonacciSpec {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("A Fibonacci publicApi") {
    describe("when asked for fibonacci number") {
      it("should return computed fibonacci number") {
        val producerRef = TestActorRef(new FibonacciProducerActor)
        val consumerRef = TestActorRef(new FibonacciConsumerActor(producerRef))
        val publicApiRef = TestActorRef(new FibonacciPublicApiActor(consumerRef))

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
