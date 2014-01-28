name := "blackjack"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-deprecation", "-feature", "-language:postfixOps"
)

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)

parallelExecution in Test := false
