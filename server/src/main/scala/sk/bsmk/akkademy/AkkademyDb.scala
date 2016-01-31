package sk.bsmk.akkademy

import akka.actor._
import akka.util.Timeout
import org.slf4j.LoggerFactory
import sk.bsmk.akkademy.messages._

import scala.collection.mutable
import scala.concurrent.duration._
import language.postfixOps
import akka.pattern.ask

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDb extends Actor with ActorLogging {

  val map = new mutable.HashMap[String, Any]

  override def receive: Receive = {

    case r: SetRequest => putAndRespond(r.key, r.value)

    case r: SetIfNotExistsRequest => handleRequest(r)

    case r: GetRequest =>
      map.get(r.key) match {
        case Some(value) => sender() ! value
        case None        => sender() ! Status.Failure(new KeyNotFoundException(r.key))
      }

    case r: DeleteRequest =>
      map.remove(r.key) match {
        case Some(value) => sender() ! value
        case None        => sender() ! Status.Failure(new KeyNotFoundException(r.key))
      }

    case o => sender() ! Status.Failure(UnknownRequestException(s"Received unknown $o"))
  }

  private def handleRequest(request: SetIfNotExistsRequest) =
    if (map.contains(request.key)) {
      sender ! Status.Failure(new KeyExistsException(request.key))
    } else {
      putAndRespond(request.key, request.value)
    }

  private def putAndRespond(key: String, value: Any): Unit = {
    log.debug("storing k:{} v:{}", key, value)
    map.put(key, value)
    sender ! Status.Success
  }

}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("akkademy")
  val actorRef = system.actorOf(Props[AkkademyDb], name = "akkademy-db")
  implicit val timeout = Timeout(5 seconds)

  log.info("Started actor with path: {}", actorRef.path)

  //actorRef ? SetIfNotExistsRequest("", "")
  //actorRef ? SetIfNotExistsRequest("", "")

}
