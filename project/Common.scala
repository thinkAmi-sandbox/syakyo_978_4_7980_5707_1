import sbt._
import Keys._
import play.sbt.PlayImport._

object Common {

  val settings = Seq(
    organization := "com.thinkami.play",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.13.3"
  )

  val dependencies = Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,

    /* database definition */
    "com.h2database" % "h2" % "1.4.197"
  )
}
