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
    debug(s"""debug!\n\tyou said "${args.message}"""")
    info(s"""info!\n\tyou said "${args.message}"""")
    warn(s"""warn!\n\tyou said "${args.message}"""")
    error(s"""error!\n\tyou said "${args.message}"""")

    Future(s"You said: ${args.message}")
  }
}
