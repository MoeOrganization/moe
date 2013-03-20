package org.moe.runtime

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.ast._

/**
 * MoeLazyEval: Class for wrapping an AST node whose evaluation is deferred.
 *
 */

class MoeLazyEval (
  interpreter: Interpreter,
  runtime: MoeRuntime,
  env: MoeEnvironment,
  node: AST
) extends MoeObject {
  def eval = {
    println("lazy eval - " + Serializer.toJSONPretty(node))
    interpreter.eval(runtime, env, node)
  }
}
