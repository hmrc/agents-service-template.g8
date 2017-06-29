import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    organization := "uk.gov.hmrc",
    scalaVersion := "2.12.1",
    resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo,
      Resolver.url("hmrc-sbt-plugin-releases",
        url("https://dl.bintray.com/hmrc/sbt-plugin-releases")
      )(Resolver.ivyStylePatterns)
    ),
    libraryDependencies ++= Seq(
      "uk.gov.hmrc" %% "play-config" % "4.3.0",
      "uk.gov.hmrc" %% "play-auditing" % "2.10.0",
      "uk.gov.hmrc" %% "play-health" % "2.1.0",
      "uk.gov.hmrc" %% "play-auth" % "1.1.0",
      "uk.gov.hmrc" %% "http-verbs" % "6.4.0",
      "uk.gov.hmrc" %% "play-health" % "2.1.0",
      "de.threedimensions" %% "metrics-play" % "2.5.13",
      "org.scalatest" %% "scalatest" % "2.2.6" % Test,
      "org.mockito" % "mockito-core" % "2.8.9" % Test,
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
    ),
    publishingSettings
  )
  .enablePlugins(PlayScala, SbtGitVersioning, SbtDistributablesPlugin)
