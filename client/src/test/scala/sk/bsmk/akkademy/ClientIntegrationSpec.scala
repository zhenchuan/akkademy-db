package sk.bsmk.akkademy

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSpecLike, Matchers}
import sk.bsmk.akkademy.messages.KeyNotFoundException

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps


/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class ClientIntegrationSpec extends FunSpecLike with Matchers  with GeneratorDrivenPropertyChecks {

  val client = new Client("127.0.0.1:2552")

  describe("akkademyDbClient") {
    it("should set a value") {
      forAll { (key: String, value: Int) =>
        client.set(key, value)
        val futureResult = client.get(key)
        val result = Await.result(futureResult, 10 seconds)
        result should equal(value)
      }
    }

    it("should return failure if key does not exist") {
      forAll { (key: String, value: Int) =>
        // to be sure we have value so we can delete it
        client.set(key, value)
        // to be sure we have no value
        client.delete(key)

        val futureResult = client.get(key)
        intercept[KeyNotFoundException] {
          Await.result(futureResult, 10 seconds)
        }
        futureResult.isCompleted shouldBe true
      }
    }
  }
}
