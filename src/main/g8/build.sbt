lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    scalaVersion := "2.12.1"
  )
  .enablePlugins(PlayScala)
