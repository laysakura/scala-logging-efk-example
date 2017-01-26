package com.github.laysakura.efk.modules

import com.github.laysakura.efk.annotations.VerboseServiceServer
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.typesafe.config.Config

object VerboseServiceConfigModule extends TwitterModule {

  @Singleton
  @Provides
  @VerboseServiceServer
  def provide(config: Config): String =
    s"${config.getString("verboseService.host")}:${config.getInt("verboseService.port")}"
}
