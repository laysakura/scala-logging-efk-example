package com.github.laysakura.efk.server

import com.github.laysakura.efk.controllers.CalculatorServiceController
import com.twitter.finatra.thrift.filters._
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
      .filter[LoggingMDCFilter]
      .filter[TraceIdMDCFilter]
      .filter[ThriftMDCFilter]
      .filter[AccessLoggingFilter]
      .filter[StatsFilter]
      .add[CalculatorServiceController]
  }
}

object CalculatorServiceServerMain$ extends CalculatorServiceServer
