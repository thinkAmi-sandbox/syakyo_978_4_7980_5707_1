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


// JDBC
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += evolutions
libraryDependencies += javaJdbc

// Anorm
// 2.6.2と2.6.3ではDownloadエラーになったので、Githubのissueにより2.6.4にしたらOKとなった
// https://github.com/playframework/anorm/issues/208
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.4"
