package com.github.laysakura.test.efk.server

import com.github.laysakura.efk.idl.VerboseService
import com.github.laysakura.efk.modules.ConfigModule
import com.twitter.finagle.Thrift
import com.twitter.inject.app.TestInjector
import com.twitter.util.{Await, Future}

class VerboseServiceTest extends com.twitter.inject.Test with com.twitter.inject.IntegrationTest {
  override val injector = TestInjector(
    modules = Seq(ConfigModule)
  )

  lazy private val client = Thrift.client.newIface[VerboseService[Future]]("localhost:4000")

  "client" should {
    "successfully call echo() API" in {
      val res = Await.result(client.echo("hi :D"))
      res shouldBe "You said: hi :D"
    }
  }
}
