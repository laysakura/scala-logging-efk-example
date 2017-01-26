package com.github.laysakura.test.efk.server

import com.github.laysakura.efk.server.CalculatorServiceServer
import com.github.laysakura.efk.idl.{VerboseService, VerboseService$FinagleClient}
import com.github.laysakura.efk.modules._
import com.github.laysakura.efk.server.VerboseServiceServer
import com.twitter.finatra.thrift.{EmbeddedThriftServer, ThriftClient}
import com.twitter.inject.app.TestInjector
import com.twitter.inject.server.FeatureTest
import com.twitter.util.{Await, Future}

class EmbeddedVerboseServiceTest extends FeatureTest {
  override val injector = TestInjector(
    modules = Seq(
      new ClientIdModule("VerboseServiceTest"),
      ConfigModule,
      VerboseServiceModule, VerboseServiceConfigModule,
      CalculatorServiceModule, CalculatorServiceConfigModule
    )
  )

  override val server = new EmbeddedThriftServer(
    twitterServer = new VerboseServiceServer
  ) with ThriftClient

  lazy val client = server.thriftClient[VerboseService[Future]]("EmbeddedVerboseServiceTest")

  "client" should {
    "successfully call echo() API" in {
      val res = Await.result(client.echo("hi :D"))
      res shouldBe "You said: hi :D"
    }
  }
}
