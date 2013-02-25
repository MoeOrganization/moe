package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class LogicalNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Not") {
    val ast = wrapSimpleAST(
      List(
        PrefixUnaryOpNode(BooleanLiteralNode(true), "!")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }

  test("... basic (false) test with Not") {
    val ast = wrapSimpleAST(
      List(
        PrefixUnaryOpNode(BooleanLiteralNode(false), "!")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic test with And") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          BooleanLiteralNode(true),
          "&&",
          BooleanLiteralNode(false)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }

  test("... basic test with nested And") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          BooleanLiteralNode(true),
          "&&",
          BinaryOpNode(
            BooleanLiteralNode(true),
            "&&",
            IntLiteralNode(100)
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 100)
  }

  test("... basic test with Or") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          BooleanLiteralNode(true),
          "||",
          BooleanLiteralNode(false)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

}
