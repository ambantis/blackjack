name := "blackjack"

val gitHeadCommitSha = settingKey[String] {
  "Determines the current git commit SHA"
}

gitHeadCommitSha := Process("git rev-parse HEAD").lines.head

version := s"1.0-${gitHeadCommitSha.value}"

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-deprecation", "-feature", "-language:postfixOps"
)

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3"
)

parallelExecution in Test := false


val makeVersionProperties = taskKey[Seq[File]]("Makes a version.properties file.")

makeVersionProperties := {
  val propFile = (resourceManaged in Compile).value / "version.properties"
  val content = "version=%s" format (gitHeadCommitSha.value)
  IO.write(propFile, content)
  Seq(propFile)
}
