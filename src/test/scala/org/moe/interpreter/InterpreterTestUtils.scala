package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

trait InterpreterTestUtils {

  def wrapSimpleAST ( nodes: List[ AST ] ) =
    CompilationUnitNode(
        ScopeNode(
            StatementsNode(
                nodes
            )
        )
    )

}
