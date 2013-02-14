package org.moe.interpreter

import org.moe.ast._

import org.scalatest.FunSuite
import org.scalatest.matchers._

import scala.util.parsing.json._

class InterpreterUtilsTestSuite extends FunSuite with ShouldMatchers {
  test("... test walking ASTs") {
    var cb_count = 0
    var node_list: List[String] = List()
    val cb: (AST) => Unit = { ast: AST =>
      cb_count += 1
      Serializer.toJSON(ast) match {
        case JSONObject(map) => node_list ++= map.keys
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
        "StatementsNode",
        "VariableDeclarationNode",
        "IntLiteralNode",
        "WhileNode",
        "BooleanLiteralNode",
        "StatementsNode",
        "IncrementNode",
        "VariableAccessNode"
      )
    )
  }
}
