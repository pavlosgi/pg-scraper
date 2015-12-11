import akka.sbt.AkkaKernelPlugin._
import org.sbtidea.SbtIdeaPlugin._
import sbt.Keys._
import sbt._
import scoverage.ScoverageSbtPlugin._
import spray.revolver.RevolverPlugin._

object MainBuild extends Build {

  val appVersion = "0.1.0-SNAPSHOT"

  val dependencies = Seq(

  )

  lazy val noPublishing = Seq(
    publish :=(),
    publishLocal :=(),
    // required until these tickets are closed https://github.com/sbt/sbt-pgp/issues/42,
    // https://github.com/sbt/sbt-pgp/issues/36
    publishTo := None
  )

  lazy val publishing = Seq(
    publishTo <<= version { (v: String) =>
      val nexus = "http://nexus.pavlosgi.com:8081/nexus/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "content/repositories/releases/")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false},
    pomExtra := (
      <url>http://www.pavlosgi.com/</url>
        <licenses>
          <license>
            <name>pg-scraper</name>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@bitbucket.org:raiden4589/pg-scraper.git</url>
          <connection>scm:git@bitbucket.org:raiden4589/pg-scraper.git</connection>
        </scm>
        <developers>
          <developer>
            <id>pavlosg</id>
            <name>Pavlos Georgiou</name>
          </developer>
        </developers>),
    credentials += Credentials("Sonatype Nexus Repository Manager",
      "0.0.0.0",
      "admin",
      "admin123")
  )

  lazy val root = Project("pg-scraper-root", file("."))
    .aggregate(pgScraperEngineCore,pgScraperExamples).settings(defaultSettings: _*).settings(noPublishing: _*)

  lazy val pgScraperEngineCore = Project("pg-scraper-core", file("core"))
    .settings(defaultSettings ++ Revolver.settings ++ instrumentSettings ++ Seq(
    libraryDependencies ++= Dependencies.all): _*)
    .settings(publishing: _*)

  lazy val pgScraperExamples = Project("pg-scraper-examples", file("examples"))
    .settings(defaultSettings ++ Revolver.settings ++ Seq(
    mainClass in(Compile, run) := Some("pavlosgi.pg_scraper.examples.TimeoutScrape")): _*)
    .settings(noPublishing: _*)
    .dependsOn(pgScraperEngineCore)

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.pavlosgi",
    version := appVersion,
    scalaVersion := "2.11.2",
    organizationName := "pavlosgi",
    resolvers ++= Seq(
      "spray" at "http://repo.spray.io/",
      "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository",
      "Local Nexus Repository Snapshots" at "http://0.0.0.0:8081/nexus/content/repositories/snapshots"
    ))

  lazy val defaultSettings = buildSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    distJvmOptions in Dist := "-Xms256M -Xmx1024M",
    outputDirectory in Dist := file("target/dist"),
    ideaExcludeFolders := ".idea" :: ".idea_modules" :: Nil)
}

object Dependencies {

  def common() = Seq(
    "org.json4s" %% "json4s-native" % "3.2.11"
  )

  val akkaVersion = "2.3.6"
  val sprayVersion = "1.3.2"

  def all() = common ++ Seq(
    "org.seleniumhq.selenium" % "selenium-java" % "2.44.0",
    "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.json4s" %% "json4s-native" % "3.2.10",
    "org.json4s" %% "json4s-ext" % "3.2.10",
    "org.mockito" % "mockito-all" % "1.9.5",
    "org.specs2" %% "specs2" % "2.3.13",
    "org.hamcrest" % "hamcrest-all" % "1.3"
  )

}