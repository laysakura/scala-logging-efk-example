package com.github.laysakura.efk.modules

import com.github.laysakura.efk.annotations.CalculatorServiceServer
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule

object CalculatorServiceConfigModule extends TwitterModule {

  @Singleton
  @Provides
  @CalculatorServiceServer
  def provide: String = "localhost:5000"
}
