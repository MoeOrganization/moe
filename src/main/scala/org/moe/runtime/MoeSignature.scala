package org.moe.runtime

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {

  def getParams = params
  def getArity  = params.length

  def bindArgsToEnv (args: MoeArguments, env: MoeEnvironment) = {
    for (i <- 0.until(getArity)) {
      params(i) match {
        case MoeNamedParameter(name)    => env.create(name, args.getArgAt(i).get)
        case MoeOptionalParameter(name) => args.getArgAt(i) match {
          case Some(a) => env.create(name, a)
          case None    => env.create(name, env.getCurrentRuntime.get.NativeObjects.getUndef)
        }
        case MoeSlurpyParameter(name)   => env.create(
          name, 
          env.getCurrentRuntime.get.NativeObjects.getArray(args.slurpArgsAt(i):_*)
        )
      }
    }
  }
}
