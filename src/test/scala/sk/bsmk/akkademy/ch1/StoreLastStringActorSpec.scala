package sk.bsmk.akkademy.ch1

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StoreLastStringActorSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {

  implicit val actorSystem = ActorSystem()

  describe("storeLastString") {
    describe("given one String message sent") {
      it("should remember the string") {
        val actorRef = TestActorRef(new StoreLastStringActor)
        actorRef ! "first-message"
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.lastReceived should equal(Some("first-message"))
      }
    }

    describe("given two String messages sent") {
      it("should remember the second string") {
        val actorRef = TestActorRef(new StoreLastStringActor)
        actorRef ! "first-message"
        actorRef ! "second-message"
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.lastReceived should equal(Some("second-message"))
      }
    }
  }
}
