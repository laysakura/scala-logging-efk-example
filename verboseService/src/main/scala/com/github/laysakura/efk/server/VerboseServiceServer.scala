package com.github.laysakura.efk.server

import com.github.laysakura.efk.controllers.VerboseServiceController
import com.github.laysakura.efk.modules.{CalculatorServiceConfigModule, CalculatorServiceModule, ClientIdModule, ConfigModule}
import com.twitter.finatra.thrift.filters.TraceIdMDCFilter
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter

class VerboseServiceServer extends ThriftServer
{
  private val THRIFT_PORT = 4000
  private val ADMIN_PORT = 4001

  override val name = "verbose-service-server"
  override val defaultFinatraThriftPort: String = s":$THRIFT_PORT"
  override def defaultHttpPort: Int = ADMIN_PORT

  override val modules = Seq(
    new ClientIdModule("VerboseServiceServer"),
    ConfigModule,
    CalculatorServiceModule, CalculatorServiceConfigModule
  )

  override def configureThrift(router: ThriftRouter) {
    router
      // TODO filterを追加し、ログを見る
//      .filter[thrift.filters.LoggingMDCFilter]
      .filter[TraceIdMDCFilter]
//      .filter[thrift.filters.ThriftMDCFilter]
//      .filter[thrift.filters.AccessLoggingFilter]
//      .filter[thrift.filters.StatsFilter]
      .add[VerboseServiceController]
  }
}

object VerboseServiceServerMain extends VerboseServiceServer
