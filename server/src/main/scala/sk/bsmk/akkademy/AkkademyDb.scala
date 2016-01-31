package sk.bsmk.akkademy

import javax.management.openmbean.KeyAlreadyExistsException

import akka.actor._
import org.slf4j.LoggerFactory
import sk.bsmk.akkademy.messages._

import scala.collection.mutable

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDb extends Actor with ActorLogging {

  val map = new mutable.HashMap[String, Any]

  private def putAndRespond(key: String, value: Any): Unit = {
    log.debug("storing k:{} v:{}", key, value)
    map.put(key, value)
    sender ! Status.Success
  }

  override def receive: Receive = {

    case r: SetRequest =>
      putAndRespond(r.key, r.value)

    case r: SetIfNotExistsRequest =>
      if (map.contains(r.key)) {
        sender ! Status.Failure(new KeyAlreadyExistsException(r.key))
      } else {
        putAndRespond(r.key, r.value)
      }

    case r: GetRequest =>
      map.get(r.key) match {
        case Some(value) => sender() ! value
        case None        => sender() ! new KeyNotFoundException(r.key)
      }

    case r: DeleteRequest =>
      map.remove(r.key) match {
        case Some(value) => sender() ! value
        case None        => sender() ! new KeyNotFoundException(r.key)
      }

    case o => sender() ! Status.Failure(UnknownMessage(s"Received unknown $o"))
  }

}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("akkademy")
  val actorRef = system.actorOf(Props[AkkademyDb], name = "akkademy-db")

  log.info("Started actor with path: {}", actorRef.path)

}
