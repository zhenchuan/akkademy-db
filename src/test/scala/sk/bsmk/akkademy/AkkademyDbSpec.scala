package sk.bsmk.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}
import sk.bsmk.akkademy.messages.SetRequest

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDbSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {

  implicit val actorSystem = ActorSystem()

  describe("akkademyDb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("key", "value")
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.get("key") should equal(Some("value"))
      }
    }
    describe("given unknown message") {
      it("should respond with failure") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! "foo"
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.size should equal(0)
      }
    }
  }
}
