package sk.bsmk.akkademy

import akka.actor.{Status, ActorSystem}
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}
import sk.bsmk.akkademy.messages.{UnknownMessage, SetRequest}
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDbSpec extends FunSpecLike with Matchers with BeforeAndAfterEach with GeneratorDrivenPropertyChecks {

  implicit val actorSystem = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  def withActorRef(testCode: (TestActorRef[AkkademyDb], AkkademyDb) => Any) {
    val actorRef = TestActorRef(new AkkademyDb)
    val akkademyDb = actorRef.underlyingActor
    akkademyDb.map.size should equal(0)
    testCode(actorRef, akkademyDb)
  }

  def withSentMessage(request: Any)(testCode: (AkkademyDb, Any) => Any): Unit = {
    withActorRef { (actorRef, akkademyDb) =>
      val future = actorRef ? request
      val response = Await.result(future, 1 second)
      testCode(akkademyDb, response)
    }
  }

  describe("akkademyDb") {
    describe("SetRequest") {
      it("should place key/value into map") {
        forAll { (key: String, value: String) =>
          withSentMessage(SetRequest(key, value)) { (akkademyDb, response) =>
            response should equal(Status.Success)
            akkademyDb.map.size should equal(1)
            akkademyDb.map.get(key) should equal(Some(value))
          }
        }
      }
      it("should respond with failure") {
        withActorRef { (actorRef, akkademyDb) =>
          val future = actorRef ? "some-string"
          intercept[UnknownMessage] {
            Await.result(future, 1 second)
          }
          akkademyDb.map.size should equal(0)
        }
      }
    }
  }

}
