package com.github.laysakura.efk.controllers

import com.github.laysakura.efk.idl
import com.github.laysakura.efk.idl.CalculatorService.Sum
import com.google.inject.{Inject, Singleton}
import com.twitter.finatra.thrift.Controller
import com.twitter.inject.Logging
import com.twitter.util.Future

@Singleton
class CalculatorServiceController @Inject() ()
  extends Controller
    with idl.CalculatorService.BaseServiceIface
    with Logging
{
  override val sum = handle(Sum) { args =>
    warn("Mmmmmmmmmmmmmmmmmm.....")
    Future(args.a + args.b)
  }
}
