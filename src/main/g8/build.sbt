import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    organization := "uk.gov.hmrc",
    scalaVersion := "2.11.11",
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.bintrayRepo("hmrc", "release-candidates"),
      Resolver.typesafeRepo("releases"),
      Resolver.jcenterRepo
    ),
    libraryDependencies ++= Seq(
      "de.threedimensions" %% "metrics-play" % "2.5.13",
      "uk.gov.hmrc" %% "govuk-template" % "5.1.0",
      "uk.gov.hmrc" %% "http-verbs" % "6.4.0",
      "uk.gov.hmrc" %% "logback-json-logger" % "3.1.0",
      "uk.gov.hmrc" %% "play-auditing" % "2.10.0",
      "uk.gov.hmrc" %% "play-auth" % "1.1.0",
      "uk.gov.hmrc" %% "play-config" % "4.3.0",
      "uk.gov.hmrc" %% "play-health" % "2.1.0",
      "uk.gov.hmrc" %% "play-ui" % "7.2.0",

      "org.scalatest" %% "scalatest" % "2.2.6" % Test,
      "org.mockito" % "mockito-core" % "2.8.9" % Test,
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
    ),
    publishingSettings
  )
  .enablePlugins(PlayScala, SbtGitVersioning, SbtDistributablesPlugin)



