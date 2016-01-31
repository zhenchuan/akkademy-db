package sk.bsmk.akkademy

import akka.actor.{Status, ActorSystem}
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}
import sk.bsmk.akkademy.messages._
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

  private def askAndAwaitResult(actorRef: TestActorRef[AkkademyDb], messageToSend: Any): Any = {
    val future = actorRef ? messageToSend
    Await.result(future, 1 second)
  }

  private def withActorRef(testCode: TestActorRef[AkkademyDb] => Any) {
    val actorRef = TestActorRef(new AkkademyDb)
    val akkademyDb = actorRef.underlyingActor
    akkademyDb.map.size should equal(0)
    testCode(actorRef)
  }

  private def withSentMessage(messageToSend: Any)(testCode: (TestActorRef[AkkademyDb], Any) => Any): Unit = {
    withActorRef { (actorRef) =>
      val result = askAndAwaitResult(actorRef, messageToSend)
      testCode(actorRef, result)
    }
  }

  describe("akkademyDb") {
    describe(SetRequest.toString()) {
      it("should place key/value into map") {
        forAll { (key: String, value: String) =>
          withSentMessage(SetRequest(key, value)) { (akkademyDbRef, response) =>
            response should equal(Status.Success)
            val akkademyDb = akkademyDbRef.underlyingActor
            akkademyDb.map.size should equal(1)
            akkademyDb.map.get(key) should equal(Some(value))
          }
        }
      }
    }
    describe(SetIfNotExistsRequest.toString()) {
      it("should place key/value into map if key does not exist") {
        forAll { (key1: String, key2: String, value: String) => whenever(key1 != key2) {
          withSentMessage(SetRequest(key1, value + "-first")) { (akkademyDbRef, response) =>
            val akkademyDb = akkademyDbRef.underlyingActor
            akkademyDb.map.size should equal(1)

            val result = askAndAwaitResult(akkademyDbRef, SetRequest(key2, value))

            result should equal(Status.Success)
            akkademyDb.map.size should equal(2)
            akkademyDb.map.get(key1) should equal(Some(value + "-first"))
            akkademyDb.map.get(key2) should equal(Some(value))
          }
        }}
      }
      it("should fail if key already exists") {
        forAll { (key: String, value: String) =>
          val request = SetIfNotExistsRequest(key, value)
          withSentMessage(request) { (akkademyDbRef, response) =>
            response should equal(Status.Success)
            val akkademyDb = akkademyDbRef.underlyingActor
            akkademyDb.map.size should equal(1)
            akkademyDb.map.get(key) should equal(Some(value))
            intercept[KeyExistsException] {
              askAndAwaitResult(akkademyDbRef, request)
            }
            akkademyDb.map.size should equal(1)
            akkademyDb.map.get(key) should equal(Some(value))
          }
        }
      }
    }
    describe(GetRequest.toString()) {
      it("should fail if key does not exists") {
        forAll { (key: String) =>
          withActorRef { (akkademyDbRef) =>
            intercept[KeyNotFoundException] {
              askAndAwaitResult(akkademyDbRef, GetRequest(key))
            }
            akkademyDbRef.underlyingActor.map.size should equal(0)
          }
        }
      }
      it("should return stored value if exists") {
        forAll { (key: String, value: String) =>
          withSentMessage(SetRequest(key, value)) { (akkademyDbRef, response) =>
            val result = askAndAwaitResult(akkademyDbRef, GetRequest(key))
            result should equal(result)
          }
        }
      }
    }

    describe(DeleteRequest.toString()) {
      it("should fail if key does not exists") {
        forAll { (key: String) =>
          withActorRef { (akkademyDbRef) =>
            intercept[KeyNotFoundException] {
              askAndAwaitResult(akkademyDbRef, DeleteRequest(key))
            }
            akkademyDbRef.underlyingActor.map.size should equal(0)
          }
        }
      }
      it("should delete stored value if exists") {
        forAll { (key: String, value: String) =>
          withSentMessage(SetRequest(key, value)) { (akkademyDbRef, response) =>
            akkademyDbRef.underlyingActor.map.size should equal(1)
            askAndAwaitResult(akkademyDbRef, DeleteRequest(key))
            akkademyDbRef.underlyingActor.map.size should equal(0)
          }
        }
      }
    }
    describe("unknown request") {
      it("should respond with failure") {
        withActorRef { (akkademyDbRef) =>
          val future = akkademyDbRef ? "some-string"
          intercept[UnknownRequestException] {
            Await.result(future, 1 second)
          }
          val akkademyDb = akkademyDbRef.underlyingActor
          akkademyDb.map.size should equal(0)
        }
      }
    }
  }
}
