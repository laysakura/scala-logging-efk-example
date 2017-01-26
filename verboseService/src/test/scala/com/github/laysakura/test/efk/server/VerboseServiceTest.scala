package com.github.laysakura.test.efk.server

import com.github.laysakura.efk.idl.VerboseService$FinagleClient
import com.github.laysakura.efk.modules._
import com.twitter.inject.app.TestInjector
import com.twitter.util.Await

class VerboseServiceTest extends com.twitter.inject.Test with com.twitter.inject.IntegrationTest {
  override val injector = TestInjector(
    modules = Seq(
      new ClientIdModule("VerboseServiceTest"),
      VerboseServiceModule, VerboseServiceConfigModule,
      CalculatorServiceModule, CalculatorServiceConfigModule
    )
  )

  private val client = injector.instance[VerboseService$FinagleClient]

  "client" should {
    "successfully call echo() API" in {
      val res = Await.result(client.echo("hi :D"))
      res shouldBe "You said: hi :D"
    }
  }
}
