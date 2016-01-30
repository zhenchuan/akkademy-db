package sk.bsmk.akkademy.ch2

import akka.actor.ActorSystem
import akka.util.Timeout
import org.scalatest.{Matchers, FunSpecLike}
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class PongActorSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(PongActor.props("response-message"))

  describe("Pong actor") {
    it("should respond with defined response") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "response-message")
    }
    it("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

}
