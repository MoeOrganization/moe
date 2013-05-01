package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.ast._

import InterpreterUtils._

object Signatures {

  def declaration (i: Interpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, ParameterNode(name, optional, slurpy, named)) => (optional, slurpy, named) match {
      case (false, false, false) => new MoePositionalParameter(name)
      case (true,  false, false) => new MoeOptionalParameter(name)
      case (false, true,  false) => new MoeSlurpyParameter(name)
      case (false, false, true)  => new MoeNamedParameter(name)
      case (false, true,  true)  => new MoeSlurpyNamedParameter(name)
      case _                     => throw new MoeErrors.InvalidParameter("parameter must be one of slurpy, optional or named")
    }

    case (env, SignatureNode(params)) => new MoeSignature(
      params.map(i.compile(env, _).asInstanceOf[MoeParameter])
    )
  }
}