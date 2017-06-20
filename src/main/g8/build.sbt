scalaVersion := "2.11.11"

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    scalaVersion := "2.12.1",
    resolvers += Resolver.bintrayRepo("hmrc", "releases"),
    libraryDependencies ++= Seq(
      "uk.gov.hmrc" %% "play-config" % "3.0.0",
      "uk.gov.hmrc" %% "play-auditing" % "2.10.0",
      "uk.gov.hmrc" %% "play-health" % "2.1.0",
      "de.threedimensions" %% "metrics-play" % "2.5.13",
      "org.scalatest" %% "scalatest" % "2.2.6" % Test,
      "org.mockito" % "mockito-core" % "2.7.22" % Test,
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
    )
  )
  .enablePlugins(PlayScala)

