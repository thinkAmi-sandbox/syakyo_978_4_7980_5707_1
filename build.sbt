name := """syakyo-app"""

Common.settings

//organization := "com.thinkami.play"
//
//version := "1.0-SNAPSHOT"

lazy val jdbcApp =
  (project in file("modules/jdbcapp"))
    .enablePlugins(PlayScala)

lazy val anormApp =
  (project in file("modules/anormapp"))
    .enablePlugins(PlayScala)

lazy val slickApp =
  (project in file("modules/slickapp"))
    .enablePlugins(PlayScala)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  // Slickを使うときは、以下のJDBC系をコメントアウト
//  .dependsOn(jdbcApp)
//  .aggregate(jdbcApp)
//  .dependsOn(anormApp)
//  .aggregate(anormApp)
  // ここまでコメントアウト
  .dependsOn(slickApp)
  .aggregate(slickApp)
