name := """akkademy-db-scala"""
organization := "sk.bsmk"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-remote" % "2.4.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5"
)

mappings in (Compile, packageBin) ~= { _.filterNot { case (_, fileName) =>
  Seq("application.conf").contains(fileName)
}}