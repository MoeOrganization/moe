package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

trait InterpreterTestUtils extends FunSuite with BeforeAndAfter {

  var runtime : MoeRuntime = _

  before {
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
