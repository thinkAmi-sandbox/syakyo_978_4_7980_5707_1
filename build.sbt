name := """syakyo-app"""
organization := "com.thinkami.play"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.thinkami.play.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.thinkami.play.binders._"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += evolutions
libraryDependencies += javaJdbc
