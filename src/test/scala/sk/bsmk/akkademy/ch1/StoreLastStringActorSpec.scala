package sk.bsmk.akkademy.ch1

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StoreLastStringActorSpec extends FunSpecLike with Matchers with BeforeAndAfterEach with GeneratorDrivenPropertyChecks {

  implicit val actorSystem = ActorSystem()

  describe("storeLastString") {
    describe("given one String message sent") {
      it("should remember the string") {
        forAll { (s: String) =>
          val actorRef = TestActorRef(new StoreLastStringActor)
          val testedRef = actorRef.underlyingActor
          testedRef.lastReceived should equal(None)
          actorRef ! s
          testedRef.lastReceived should equal(Some(s))
        }
      }
    }

    describe("given two String messages sent") {
      it("should remember the second string") {
        forAll { (s1: String, s2: String) =>
          val actorRef = TestActorRef(new StoreLastStringActor)
          val testedRef = actorRef.underlyingActor
          testedRef.lastReceived should equal(None)
          actorRef ! s1
          actorRef ! s2
          testedRef.lastReceived should equal(Some(s2))
        }
      }
    }
  }
}
