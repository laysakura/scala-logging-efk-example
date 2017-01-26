import com.typesafe.sbt.packager.docker._

lazy val fluentDirectoryTask = TaskKey[File]("fluentDirectory")
lazy val copyFluentFilesTask = TaskKey[Unit]("copyFluentFiles")

lazy val commonSettings = Seq(
  organization := "com.github.laysakura",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
  version := (version in ThisBuild).value,
  parallelExecution in ThisBuild := false,
  resolvers += ("twitter" at "https://maven.twttr.com")
)

lazy val fluentdDockerSettings = Seq(
  copyFluentFilesTask := {
    IO.copyFile(fluentDirectoryTask.value / "fluent.conf", (stage in Docker).value / "fluent.conf")
    IO.copyDirectory(fluentDirectoryTask.value / "plugins", (stage in Docker).value / "plugins")
  },

  publishLocal in Docker := {
    copyFluentFilesTask.value
    (publishLocal in Docker).value
  },

  dockerBaseImage := "fluent/fluentd:latest-onbuild",

  // fluentdの立ち上げ前に必要なセットアップと、コンパイルしたServiceのセットアップ
  dockerCommands := Seq(
    Cmd("FROM", "fluent/fluentd:latest-onbuild"),
    Cmd("USER", "root"),

    ExecCmd("RUN", "apk", "update"),
    ExecCmd("RUN", "apk", "add", "build-base"),
    ExecCmd("RUN", "apk", "add", "ruby-dev"),

    Cmd("USER", "fluent"),
    Cmd("WORKDIR", "/home/fluent"),
    Cmd("ENV", "PATH", "/home/fluent/.gem/ruby/2.3.0/bin:$PATH"),

    ExecCmd("RUN", "gem", "install", "fluent-plugin-elasticsearch"),

    ExecCmd("RUN", "rm", "-rf", "/home/fluent/.gem/ruby/2.3.0/cache/*.gem"),
    ExecCmd("RUN", "gem", "sources", "-c"),

    Cmd("USER", "root"),
    ExecCmd("RUN", "apk", "del", "build-base"),
    ExecCmd("RUN", "apk", "del", "ruby-dev"),

    Cmd("ADD", "opt /opt")
  )
)

lazy val versions = new {
  val logback = "1.1.7"
  val `logstash-logback-encoder` = "4.8"
  val finagle = "6.40.0"
  val finatra = "2.6.0"
  val scrooge = "4.12.0"
  val `typesafe-config` = "1.3.1"

  val guice = "4.0"
  val scalatest = "3.0.1"
  val specs = "2.3.12"
}

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  aggregate(
    common,
    verboseService,
    calculatorService
  )

lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "net.logstash.logback" % "logstash-logback-encoder" % versions.`logstash-logback-encoder`,
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "com.twitter" %% "scrooge-core" % versions.scrooge,
      "com.twitter" %% "inject-core" % versions.finatra,
      "com.twitter" %% "inject-app" % versions.finatra,
      "com.typesafe" % "config" % versions.`typesafe-config`,

      "org.specs2" %% "specs2" % versions.specs % "test",
      "org.scalatest" %% "scalatest" % versions.scalatest % "test",

      // for TestInjector
      "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",
      "com.twitter" %% "finatra-thrift" % versions.finatra % "test",
      "com.twitter" %% "finatra-thrift" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-app" % versions.finatra % "test",
      "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-core" % versions.finatra % "test",
      "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-modules" % versions.finatra % "test",
      "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-server" % versions.finatra % "test",
      "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests"
    )
  )

lazy val verboseService = (project in file("verboseService")).
  enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin).
  settings(commonSettings: _*).
    settings(
    fluentDirectoryTask := { baseDirectory.value / ".." / "docker" / "app1" / "fluentd" }
  ).
  settings(fluentdDockerSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-thrift" % versions.finagle,
      "com.twitter" %% "finagle-core" % versions.finagle
    ),
    javaOptions in Universal ++= Seq(
      "-Dlog.service.output=verboseService.log"
    )
  ).
  aggregate(verboseServiceIdl).
  dependsOn(
    verboseServiceIdl % "test->test;compile->compile",
    calculatorServiceIdl % "test->test;compile->compile"
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
  ).
  aggregate(common).
  dependsOn(common % "test->test;compile->compile")

lazy val calculatorService = (project in file("calculatorService")).
  enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin).
  settings(commonSettings: _*).
  settings(
    fluentDirectoryTask := { baseDirectory.value / ".." / "docker" / "app2" / "fluentd" }
  ).
  settings(fluentdDockerSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-thrift" % versions.finagle,
      "com.twitter" %% "finagle-core" % versions.finagle
    ),
    javaOptions in Universal ++= Seq(
      "-Dlog.service.output=calculatorService.log"
    )
  ).
  aggregate(calculatorServiceIdl).
  dependsOn(calculatorServiceIdl % "test->test;compile->compile")

lazy val calculatorServiceIdl = (project in file("calculatorServiceIdl")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finatra-thrift" % versions.finatra,
      "com.twitter" %% "scrooge-core" % versions.scrooge
    ),
    scroogeThriftSourceFolder in Compile <<= baseDirectory { base => base / "src/main/thrift" },
    scroogeThriftDependencies in Compile := Seq("finatra-thrift_2.11")
  ).
  aggregate(common).
  dependsOn(common % "test->test;compile->compile")

