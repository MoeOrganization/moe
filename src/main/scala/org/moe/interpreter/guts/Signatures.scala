package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.ast._

object Signatures {

  def declaration (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, ParameterNode(name, optional, slurpy, named, default)) => (optional, slurpy, named, default) match {
      case (false, false, false, None)          => new MoePositionalParameter(name)
      case (true,  false, false, None)          => new MoeOptionalParameter(name)
      case (false, true,  false, None)          => new MoeSlurpyParameter(name)
      case (false, false, true,  None)          => new MoeNamedParameter(name)
      case (false, true,  true,  None)          => new MoeSlurpyNamedParameter(name)
      case (false, false, false, Some(def_val)) => new MoeDefaultValueParameter(name, i.evaluate(env, def_val))
      case _                                    => throw new MoeErrors.InvalidParameter("parameter must be one of slurpy, optional, named or default-value")
    }

    case (env, SignatureNode(params)) => new MoeSignature(
      params.map(i.compile(env, _).asInstanceOf[MoeParameter])
    )
  }
}
