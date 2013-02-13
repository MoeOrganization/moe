package org.moe.interpreter

import org.moe.ast._

import org.scalatest.FunSuite
import org.scalatest.matchers._

import scala.util.parsing.json._

class InterpreterUtilsTestSuite extends FunSuite with ShouldMatchers {
  test("... test walking ASTs") {
    var cb_count = 0
    var node_list: List[String] = List()
    val cb: (AST, Int) => Unit = { (ast, level) =>
      cb_count += 1
      Serializer.toJSON(ast) match {
        case JSONObject(map) => node_list ++= map.keys.map(level.toString + "-" + _)
      }
    }
    InterpreterUtils.walkAST(
      StatementsNode(
        List(
          VariableDeclarationNode("$n", IntLiteralNode(0)),
          WhileNode(
            BooleanLiteralNode(true),
            StatementsNode(
              List(
                IncrementNode(VariableAccessNode("$n"))
              )
            )
          )
        )
      ),
      cb
    )

    cb_count should equal (8)
    node_list should equal (
      List(
        "0-StatementsNode",
        "1-VariableDeclarationNode",
        "2-IntLiteralNode",
        "1-WhileNode",
        "2-BooleanLiteralNode",
        "2-StatementsNode",
        "3-IncrementNode",
        "4-VariableAccessNode"
      )
    )
  }
}
