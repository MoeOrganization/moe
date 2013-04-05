package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers._

trait InterpreterTestUtils
  extends FunSuite
  with BeforeAndAfter
  with ShouldMatchers {

  var interpreter : Interpreter = _
  var runtime : MoeRuntime = _

  before {
    interpreter = new Interpreter()
    runtime = new MoeRuntime()
    runtime.bootstrap()
  }

  def wrapSimpleAST ( nodes: List[ AST ] ) =
    CompilationUnitNode(
      ScopeNode(
        StatementsNode(nodes)
      )
    )

}
