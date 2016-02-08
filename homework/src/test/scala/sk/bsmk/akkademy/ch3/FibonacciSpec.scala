package sk.bsmk.akkademy.ch3

import akka.actor.ActorSystem
import akka.util.Timeout
import org.scalatest.{Matchers, FunSpecLike}

import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 08/02/16.
  */
trait FibonacciSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  def defaultSleep(): Unit = {
    Thread.sleep(100)
  }

  val fibonacciNumbers = Map(
    (1,  1),
    (2,  1),
    (3,  2),
    (4,  3),
    (5,  5),
    (6,  8),
    (7,  13),
    (8,  21),
    (9,  34),
    (10, 55)
  )

  val orderForTest = 10

}
