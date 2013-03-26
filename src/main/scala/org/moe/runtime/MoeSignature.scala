package org.moe.runtime

import scala.util.{Try, Success, Failure}

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {

  def getParams = params
  def getArity  = params.length

  def checkArguments (args: MoeArguments) = {
    if (getArity != args.getArgCount) 
      throw new MoeErrors.MoeProblems("not enought arguments")
    // TODO - check types (based on sigils)
  }

  def bindArgsToEnv (args: MoeArguments, env: MoeEnvironment) = {
    for (i <- 0.until(getArity)) {
      params(i) match {
        case MoeNamedParameter(name)    => env.create(name, args.getArgAt(i))
        case MoeOptionalParameter(name) => env.create(name, args.getArgAt(i))
        case MoeSlurpyParameter(name)   => env.create(name, args.getArgAt(i))
      }
    }
  }
}
