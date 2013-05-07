package org.moe.runtime

import scala.collection.mutable.{HashMap,Map}

import org.moe.runtime.nativeobjects.MoePairObject

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {

  lazy val arity = params.length
  lazy val namedParameterMap: Map[String,MoeParameter] = Map(
    params.filter(_ match { 
      case (x: MoeNamedParameter) => true
      case _                      => false
    }).map(
      p => p.getKeyName -> p
    ):_*
  )

  private def checkType (n: String, o: MoeObject) = {
    if (!MoeType.checkType(n, o)) throw new MoeErrors.IncompatibleType(
        "the argument (" + n + ") is not compatible with " + o.getAssociatedType.map(_.getName).getOrElse("NO TYPE")
      )
  }

  def getParams = params

  def bindArgsToEnv (args: MoeArguments, env: MoeEnvironment) = {
    
    val r = env.getCurrentRuntime.get

    var extra: List[MoeObject] = List()

    for (i <- 0.until(arity)) {
      params(i) match {
        case MoePositionalParameter(name) => {
          val arg = args.getArgAt(i).getOrElse(
            throw new MoeErrors.MissingParameter(name)
          )
          checkType(name, arg)
          env.create(name, arg)
        }
        case MoeOptionalParameter(name) => args.getArgAt(i) match {
          case Some(a) => {
            checkType(name, a)
            env.create(name, a)
          }
          case None => {
            // no need to check type because
            // we know that undef will pass
            env.create(name, r.NativeObjects.getUndef)
          }
        }
        case MoeSlurpyParameter(name) => env.create(
          name, 
          r.NativeObjects.getArray(args.slurpArgsAt(i):_*)
        )
        case MoeSlurpyNamedParameter(name) => env.create(
          name, 
          r.NativeObjects.getHash(args.slurpArgsAt(i).map(_.unboxToTuple.get):_*)
        )
        case _ => extra = args.getArgAt(i).get :: extra 
      }
    }

    if (!args.wereAllArgsConsumed) {
      throw new MoeErrors.InvalidParameter(
        "Not all arguments were consumed " + args.consumedArgCount + " were consumed, but we had " + args.getArgCount
      )
    }

    for (arg <- extra) {
      arg match {
        case (a: MoePairObject) => {
          val k = a.key(r).unboxToString.get
          val p = namedParameterMap.get(k).getOrElse(
            throw new MoeErrors.MissingParameter("Could not find matching parameter key for " + k)
          )
          val v = a.value(r)
          val n = p.getName
          checkType(n, v)
          env.create(n, v)
        }
        case _ => throw new MoeErrors.IncompatibleType("argument was not a pair")
      }
    }

  }
}
