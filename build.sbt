lazy val buildSettings = Seq{
  organization := "sk.bsmk"
  version := "0.1.0-SNAPSHOT"
  scalaVersion := "2.11.7"
}


lazy val db = (project in file(".")).
  settings(buildSettings: _*).
  settings(name := """akkademy-db-scala""").
  aggregate(server)


lazy val messages = (project in file("messages")).
  settings(buildSettings: _*).
  settings(
    name := """akkademy-db-messages"""
  )

lazy val server = (project in file("server")).
  dependsOn(messages).
  settings(buildSettings: _*).
  settings(
    name := """akkademy-db-server""",
    libraryDependencies ++= commonDependencies
  )

lazy val client = (project in file("client")).
  dependsOn(messages).
  settings(buildSettings: _*).
  settings(
    name := """akkademy-db-client""",
    libraryDependencies ++= commonDependencies
  )


lazy val homework = (project in file("homework")).
  settings(buildSettings: _*).
  settings(
    name := """akkademy-db-homework""",
    libraryDependencies ++= commonDependencies
  )

lazy val akkaVersion = "2.4.1"

lazy val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5"
)

/*
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle
*/