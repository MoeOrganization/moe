package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import InterpreterUtils._

object CompilationUnits {

  def apply (i: Interpreter, r: MoeRuntime, env: MoeEnvironment): PartialFunction[AST, MoeObject] = {
    case CompilationUnitNode(body) => i.eval(r, env, body)
    case ScopeNode(body) => i.eval(r, new MoeEnvironment(Some(env)), body)
    case StatementsNode(nodes) => {
      nodes.foldLeft[MoeObject](r.NativeObjects.getUndef)(
        (_, node) => i.eval(r, env, node)
      )
    }
  }
}