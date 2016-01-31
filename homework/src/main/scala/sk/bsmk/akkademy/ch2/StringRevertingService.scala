package sk.bsmk.akkademy.ch2

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern.ask
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
object StringRevertingService {

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val actorRef = system.actorOf(Props[StringRevertingActor])

  def revert(toRevert: Any): Future[Any] = {
    actorRef ? toRevert
  }

}
