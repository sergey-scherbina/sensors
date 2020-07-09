name := "sensors"

version := "0.1"

scalaVersion := "2.12.11"

val AkkaVersion = "2.6.6"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "2.0.1",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
  "org.slf4j" % "slf4j-simple" % "1.7.30" % Test
)

scalacOptions += "-Ypartial-unification"