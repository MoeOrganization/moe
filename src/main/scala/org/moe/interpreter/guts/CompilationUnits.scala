package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object CompilationUnits {

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, CompilationUnitNode(body)) => i.eval(r, env, body)
    case (env, ScopeNode(body, false))    => i.compile_and_evaluate(env, body)
    case (env, ScopeNode(body, true))     => i.compile_and_evaluate(new MoeEnvironment(Some(env)), body)
    case (env, StatementsNode(nodes))     => {
      nodes.foldLeft[MoeObject](r.NativeObjects.getUndef)(
        (_, node) => i.compile_and_evaluate(env, node)
      )
    }
  }
}
