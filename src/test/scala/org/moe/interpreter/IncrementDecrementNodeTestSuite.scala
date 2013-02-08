package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class IncrementDecrementNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with variable increment") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 43)
  }

  test("... basic test with variable increment (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 99.6)
  }

  test("... basic test with variable decrement") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        DecrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 41)
  }

  test("... basic test with variable decrement (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        DecrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 97.6)
  }

}
