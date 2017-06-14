scalaVersion := "2.11.11"

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    scalaVersion := "2.12.1",
    libraryDependencies ++= Seq(
      filters,
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
    )
  )
  .enablePlugins(PlayScala)
