package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object CompilationUnits {

  def apply (i: Interpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, CompilationUnitNode(body)) => i.eval(r, env, body)
    case (env, ScopeNode(body))           => i.eval(r, new MoeEnvironment(Some(env)), body)
    case (env, StatementsNode(nodes))     => {
      nodes.foldLeft[MoeObject](r.NativeObjects.getUndef)(
        (_, node) => i.eval(r, env, node)
      )
    }
  }
}