package com.github.laysakura.test.efk.server

import com.github.laysakura.efk.idl.VerboseService
import com.github.laysakura.efk.modules.ConfigModule
import com.github.laysakura.efk.server.VerboseServiceServer
import com.twitter.finatra.thrift.{EmbeddedThriftServer, ThriftClient}
import com.twitter.inject.app.TestInjector
import com.twitter.inject.server.FeatureTest
import com.twitter.util.{Await, Future}

class VerboseServiceTest extends FeatureTest {
  override val injector = TestInjector(
    modules = Seq(ConfigModule)
  )

  override val server = new EmbeddedThriftServer(
    twitterServer = new VerboseServiceServer
  ) with ThriftClient

  lazy val client = server.thriftClient[VerboseService[Future]]("featureTestClient")

  "client" should {
    "successfully call echo() API" in {
      val res = Await.result(client.echo("hi :D"))
      res shouldBe "You said: hi :D"
    }
  }
}
