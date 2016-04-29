val dependencies = Seq(
  jdbc,
  evolutions,
  "com.typesafe.play" %% "play-slick" % "2.0.0"
)

val settings = Seq(
  name := "slick-3.1-example",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-unchecked", "-deprecation", "-feature",
    "-encoding", "utf8",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused"
  ),
  libraryDependencies ++= dependencies
)

lazy val root = (
  project in file(".")
  enablePlugins(PlayScala)
  settings(settings)
)
