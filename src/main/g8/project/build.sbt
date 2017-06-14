lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    scalaVersion := "2.12.1"
  )
  .enablePlugins(PlayScala)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.12")
