name := """jdbcapp"""

Common.settings

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
) ++ Common.dependencies
