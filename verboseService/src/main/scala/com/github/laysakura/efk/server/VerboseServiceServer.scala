package com.github.laysakura.efk.server

import com.github.laysakura.efk.controllers.VerboseServiceController
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter

class VerboseServiceServer extends ThriftServer {
  override val name = "verbose-service-server"
  override val defaultFinatraThriftPort: String = ":4000"
  override def defaultHttpPort: Int = 4001

  override def configureThrift(router: ThriftRouter) {
    router
      // TODO filterを追加し、ログを見る
//      .filter[thrift.filters.LoggingMDCFilter]
//      .filter[thrift.filters.TraceIdMDCFilter]
//      .filter[thrift.filters.ThriftMDCFilter]
//      .filter[thrift.filters.AccessLoggingFilter]
//      .filter[thrift.filters.StatsFilter]
      .add[VerboseServiceController]
  }
}

object VerboseServiceServerMain extends VerboseServiceServer
