name := """InterviewTask"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  evolutions,
  "com.h2database" % "h2" % "2.2.224",
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
//  "mysql" % "mysql-connector-java" % "5.1.18",
  "net.codingwell" %% "scala-guice" % "6.0.0",
)
