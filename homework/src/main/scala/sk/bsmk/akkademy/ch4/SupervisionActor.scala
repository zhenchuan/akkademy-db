package sk.bsmk.akkademy.ch4

import java.io.FileNotFoundException

import akka.actor.SupervisorStrategy.{Escalate, Stop, Restart, Resume}
import akka.actor.{OneForOneStrategy, SupervisorStrategy, ActorLogging, Actor}

/**
  * Created by miroslav.matejovsky on 10/02/16.
  */
class SupervisionActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case _ => log.info("I got something")
  }

  override def supervisorStrategy: SupervisorStrategy = { OneForOneStrategy() {
      //case FileNotFoundException => Resume
      //case NumberFormatException => Restart
      //case IndexOutOfBoundsException => Stop
      case _ => Escalate
    }
  }

}
