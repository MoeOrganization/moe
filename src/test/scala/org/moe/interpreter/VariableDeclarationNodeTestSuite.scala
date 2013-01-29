package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class VariableDeclarationNodeTestSuite extends FunSuite with BeforeAndAfter with InterpreterTestUtils {

  test("... basic test with variable declaration") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 42)
  }

  test("... basic test with variable assignment") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        VariableAssignmentNode(
          "$foo",
          StringLiteralNode("jason")
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "jason")
  }

}
