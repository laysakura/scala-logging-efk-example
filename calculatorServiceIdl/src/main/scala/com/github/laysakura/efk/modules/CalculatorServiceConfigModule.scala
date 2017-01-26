package com.github.laysakura.efk.modules

import com.github.laysakura.efk.annotations.CalculatorServiceServer
import com.google.inject.{Inject, Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.typesafe.config.Config

object CalculatorServiceConfigModule extends TwitterModule {

  @Singleton
  @Provides
  @CalculatorServiceServer
  def provide(config: Config): String =
    s"${config.getString("calculatorService.host")}:${config.getInt("calculatorService.port")}"
}
