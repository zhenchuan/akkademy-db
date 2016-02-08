package sk.bsmk.akkademy.ch3

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.scalatest.{Matchers, FunSpecLike}
import sk.bsmk.akkademy.ch3.messages.TellFibonacciNumber
import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
class FibonacciConsumerActorSpec extends FunSpecLike with Matchers with FibonacciTestHelper {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("when asked for fibonacci number") {
    it("should return one") {
      val producerRef = TestActorRef(new FibonacciProducerActor)
      val consumerRef = TestActorRef(new FibonacciConsumerActor(producerRef))

      consumerRef.underlyingActor.actualNumber should equal(Option.empty)

      consumerRef ! TellFibonacciNumber(orderForTest)

      // KILL ME!!
      Thread.sleep(100)

      consumerRef.underlyingActor.actualNumber should equal(Some(fibonacciNumbers(orderForTest)))

      //fibonacciNumber should equal(ComputedFibonacciNumber(55))
      //actorRef.underlyingActor.numbers.size should equal(10)
    }
  }

}
