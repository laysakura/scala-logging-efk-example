lazy val commonSettings = Seq(
  organization := "com.github.laysakura",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
  version := (version in ThisBuild).value,
  parallelExecution in ThisBuild := false,
  resolvers += ("twitter" at "https://maven.twttr.com")
)

lazy val versions = new {
  val logback = "1.1.7"
  val finagle = "6.40.0"
  val finatra = "2.6.0"
  val scrooge = "4.12.0"

  val guice = "4.0"
  val scalatest = "3.0.1"
  val specs = "2.3.12"
}

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  aggregate(
    common,
    verboseService
  )

lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "com.twitter" %% "scrooge-core" % versions.scrooge,

      "org.specs2" %% "specs2" % versions.specs % "test",
      "org.scalatest" %% "scalatest" % versions.scalatest % "test"
    )
  )

lazy val verboseService = (project in file("verboseService")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-thrift" % versions.finagle,
      "com.twitter" %% "finagle-core" % versions.finagle
    )
  )

lazy val verboseServiceIdl = (project in file("verboseServiceIdl")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finatra-thrift" % versions.finatra,
      "com.twitter" %% "scrooge-core" % versions.scrooge
    ),
    scroogeThriftSourceFolder in Compile <<= baseDirectory { base => base / "src/main/thrift" },
    scroogeThriftDependencies in Compile := Seq("finatra-thrift_2.11")
  )
