package com.github.laysakura.efk.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.typesafe.config.{Config, ConfigFactory}

object ConfigModule extends TwitterModule {

  @Singleton
  @Provides
  def provideConfig: Config = {
    ConfigFactory.load()
  }
}
