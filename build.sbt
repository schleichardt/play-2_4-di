name := """play-2_4-di"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

libraryDependencies ++=
  cache ::
  javaWs ::
  ("io.sphere.sdk.jvm" % "models" % "1.0.0-M12" withSources()) ::
  "org.easytesting" % "fest-assert" % "1.4" % "test" ::
  Nil

javacOptions += "-Xlint:deprecation"