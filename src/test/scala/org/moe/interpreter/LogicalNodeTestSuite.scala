package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class LogicalNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Not") {
    val ast = wrapSimpleAST(
      List(
        NotNode(BooleanLiteralNode(true))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[ MoeBooleanObject ].getNativeValue === false)
  }

  test("... basic (false) test with Not") {
    val ast = wrapSimpleAST(
      List(
        NotNode(BooleanLiteralNode(false))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with And") {
    val ast = wrapSimpleAST(
      List(
        AndNode(
          BooleanLiteralNode(true),
          BooleanLiteralNode(false)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with nested And") {
    val ast = wrapSimpleAST(
      List(
        AndNode(
          BooleanLiteralNode(true),
          AndNode(
            BooleanLiteralNode(true),
            IntLiteralNode(100)
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 100)
  }

  test("... basic test with Or") {
    val ast = wrapSimpleAST(
      List(
        OrNode(
          BooleanLiteralNode(true),
          BooleanLiteralNode(false)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

}
