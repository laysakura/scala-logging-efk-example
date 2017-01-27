package com.github.laysakura.efk.controllers

import com.github.laysakura.efk.idl
import com.github.laysakura.efk.idl.CalculatorService$FinagleClient
import com.github.laysakura.efk.idl.VerboseService.Echo
import com.google.inject.{Inject, Singleton}
import com.twitter.finatra.thrift.Controller
import com.twitter.inject.Logging
import com.twitter.util.{Await, Future}

import scala.util.{Failure, Success, Try}

@Singleton
class VerboseServiceController @Inject() (
  calcClient: CalculatorService$FinagleClient
)
  extends Controller
    with idl.VerboseService.BaseServiceIface
    with Logging
{
  override val echo = handle(Echo) { args =>
    debug(s"""debug!\n\tyou said "${args.message}"""")
    info(s"""info!\n\tyou said "${args.message}"""")
    warn(s"""warn!\n\tyou said "${args.message}"""")
    error(s"""error!\n\tyou said "${args.message}"""")

    warn(s"""Verbose "WHAT IS 1+1, Mr. Calc?"Mr. Calc "Mmm... It's ${Await.result(calcClient.sum(1, 1))}!!"\n""")

    Try(throwsError) match {
      case Success(_) =>
      case Failure(e) => error(s"Got error from throwsError() : $e")
    }

    Future(s"You said: ${args.message}")
  }

  private def throwsError: Int = 1 / 0
}
