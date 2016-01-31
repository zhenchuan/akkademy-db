package sk.bsmk.akkademy.ch2

import akka.actor.Status
import org.scalatest.{Matchers, FunSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StringRevertingServiceSpec extends FunSpecLike with Matchers {

  describe("Reverting service") {
    it("should return reversed string") {
      val toSend = "something-for-reversal"
      val future = StringRevertingService.revert(toSend)
      val reversed = Await.result(future, 1 second)
      reversed should equal(toSend.reverse)
    }
    it("should fail if something else than String is sent") {
      val future = StringRevertingService.revert(1234)
      val result = Await.result(future, 1 second)
      result should equal(Status.Failure)
    }
  }

}
