package com.github.laysakura.efk.modules

import com.github.laysakura.efk.annotations.CalculatorServiceServer
import com.github.laysakura.efk.annotations
import com.github.laysakura.efk.idl.{CalculatorService, CalculatorService$FinagleClient}
import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.thrift.ClientId
import com.twitter.inject.TwitterModule
import com.twitter.util.Future

object CalculatorServiceModule extends TwitterModule {
  val ServiceName = "verbose-service"

  @Provides
  @Singleton
  def provideCalculatorServiceFinagleClient(client: CalculatorService[Future]): CalculatorService$FinagleClient =
    client.asInstanceOf[CalculatorService$FinagleClient]

  @Provides
  @Singleton
  def provideCalculatorService(
    @CalculatorServiceServer addr: String,
    @annotations.ClientId clientId: String
  ): CalculatorService[Future] = {
    val thriftClient = ThriftMux.Client().withClientId(ClientId(clientId))
    thriftClient.newIface[CalculatorService[Future]](addr, ServiceName)
  }
}
