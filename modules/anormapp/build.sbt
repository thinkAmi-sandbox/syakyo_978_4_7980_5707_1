name := """anormapp"""
Common.settings

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  // 2.6.2と2.6.3ではDownloadエラーになったので、Githubのissueにより2.6.4にしたらOKとなった
  // https://github.com/playframework/anorm/issues/208
  "org.playframework.anorm" %% "anorm" % "2.6.4"
) ++ Common.dependencies
