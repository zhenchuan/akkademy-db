package sk.bsmk.akkastreams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ClosedShape, ActorMaterializer}
import akka.stream.scaladsl._
import org.slf4j.LoggerFactory
import sk.bsmk.akkastreams.data.DataProvider

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by miroslav.matejovsky on 10/02/16.
  */
object MainApp extends App {

  val log = LoggerFactory.getLogger(MainApp.getClass)

  final case class Author(handle: String)

  final case class Hashtag(name: String)

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashtags: Set[Hashtag] = body.split(" ").collect {
      case t if t.startsWith("#") => Hashtag(t)
    }.toSet
  }

  val akka = Hashtag("#akka")

  implicit val system = ActorSystem("akka-streams-actor-system")
  implicit val materializer = ActorMaterializer()

  val tweetList = List(
    Tweet(DataProvider.barAuthor, System.currentTimeMillis(), "interesting stuff #akka"),
    Tweet(DataProvider.fooAuthor, System.currentTimeMillis(), "blah blah #bullshit")
  )

  val tweets: Source[Tweet, NotUsed] = Source(tweetList)

  val authors: Source[Author, NotUsed] = tweets.filter(_.hashtags.contains(akka)).map(_.author)

  authors.runWith(Sink.foreach(author => log.info("Author tweeting about akka: {}", author)))


  val writeAuthors: Sink[Author, NotUsed] = ???
  val writeHashtags: Sink[Hashtag, NotUsed] = ???

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit  b =>
    import GraphDSL.Implicits._

    val bcast = b.add(Broadcast[Tweet](2))
    tweets ~> bcast.in
    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags
    ClosedShape
  })

  g.run()


  val termination = system.terminate()
  Await.result(termination, 5 seconds)

}
