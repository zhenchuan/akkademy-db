package sk.bsmk.akkademy.ch2

import akka.actor.Status
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, FunSpecLike}

import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class StringRevertingServiceSpec extends FunSpecLike with Matchers with GeneratorDrivenPropertyChecks {

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
    it("reverses list of strings") {
      forAll { strings: List[String] =>
        val futures: List[Future[Any]] = strings.map(StringRevertingService.revert)
        val futureOfLists = Future.sequence(futures)
        val reversed = Await.result(futureOfLists, 1 second)
        strings.zip(reversed).foreach {
          case (in, out) => out should equal(in.reverse)
        }
      }
    }
  }

}
