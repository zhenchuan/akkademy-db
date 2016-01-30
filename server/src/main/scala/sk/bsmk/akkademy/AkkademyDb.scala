package sk.bsmk.akkademy

import akka.actor._
import org.slf4j.LoggerFactory
import sk.bsmk.akkademy.messages.{KeyNotFoundException, GetRequest, SetRequest}

import scala.collection.mutable

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
class AkkademyDb extends Actor with ActorLogging {

  val map = new mutable.HashMap[String, Object]

  override def receive: Receive = {
    case SetRequest(key, value) =>
      log.info("received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
      sender() ! Status.Success

    case GetRequest(key) =>
      log.info("received GetRequest - key: {}", key)
      val response = map.get(key)
      response match {
        case Some(value) => sender() ! value
        case None        => sender() ! new KeyNotFoundException(key)
      }

    case o => Status.Failure(new ClassNotFoundException)
    //case o => sender() ! Status.Failure(new ClassNotFoundException)
  }

}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("akkademy")
  val actorRef = system.actorOf(Props[AkkademyDb], name = "akkademy-db")

  log.info("Started actor with path: {}", actorRef.path)

}
