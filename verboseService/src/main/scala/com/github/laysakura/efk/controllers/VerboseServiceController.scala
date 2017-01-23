package com.github.laysakura.efk.controllers

import com.github.laysakura.efk.idl
import com.github.laysakura.efk.idl.VerboseService.Echo
import com.google.inject.{Inject, Singleton}
import com.twitter.finatra.thrift.Controller
import com.twitter.inject.Logging
import com.twitter.util.Future

@Singleton
class VerboseServiceController @Inject() ()
  extends Controller
    with idl.VerboseService.BaseServiceIface
    with Logging
{
  override val echo = handle(Echo) { args =>
    debug(s"debug: you said ${args.message}")
    info(s"info: you said ${args.message}")
    warn(s"warn: you said ${args.message}")
    error(s"error: you said ${args.message}")

    Future(s"You said: ${args.message}")
  }
}
