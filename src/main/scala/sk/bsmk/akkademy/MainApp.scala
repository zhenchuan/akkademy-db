package sk.bsmk.akkademy

import akka.actor.{Props, ActorSystem}

/**
  * Created by miroslav.matejovsky on 30/01/16.
  */
object MainApp extends App {

  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDb], name = "akkademy-db")

}
