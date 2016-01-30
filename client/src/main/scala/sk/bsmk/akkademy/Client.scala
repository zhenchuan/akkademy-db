package sk.bsmk.akkademy

import akka.actor.ActorSystem
import akka.util.Timeout
import sk.bsmk.akkademy.messages.{SetRequest, GetRequest}
import scala.concurrent.Future
import scala.concurrent.duration._
import language.postfixOps
import akka.pattern.ask


/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class Client(remoteAddress: String) {

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key: String, value: Object): Future[Any] = {
    remoteDb ? SetRequest(key, value)
  }

  def get(key: String): Future[Any] = {
    remoteDb ? GetRequest(key)
  }

}
