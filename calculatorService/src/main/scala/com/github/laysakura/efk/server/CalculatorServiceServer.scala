package com.github.laysakura.efk.server

import com.github.laysakura.efk.controllers.CalculatorServiceController
import com.twitter.finatra.thrift.filters.{LoggingMDCFilter, ThriftMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter

class CalculatorServiceServer extends ThriftServer
{
  private val THRIFT_PORT = 5000
  private val ADMIN_PORT = 5001

  override val name = "calculator-service-server"
  override val defaultFinatraThriftPort = s":$THRIFT_PORT"
  override def defaultHttpPort = ADMIN_PORT

  override def configureThrift(router: ThriftRouter) {
    router
      // TODO filterを追加し、ログを見る
      .filter[LoggingMDCFilter]
      .filter[TraceIdMDCFilter]
      .filter[ThriftMDCFilter]
//      .filter[thrift.filters.AccessLoggingFilter]
//      .filter[thrift.filters.StatsFilter]
      .add[CalculatorServiceController]
  }
}

object CalculatorServiceServerMain$ extends CalculatorServiceServer
