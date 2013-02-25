package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class ComparisonNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with <") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          IntLiteralNode(4),
          "<",
          IntLiteralNode(6)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic (false) test with <") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          IntLiteralNode(6),
          "<",
          IntLiteralNode(4)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }

  test("... basic test with < between int and float") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          IntLiteralNode(4),
          "<",
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic test with < of equal floats") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          FloatLiteralNode(6.433),
          "<",
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }

  test("... basic test with >") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          IntLiteralNode(6),
          ">",
          IntLiteralNode(4)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic (false) test with >") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          IntLiteralNode(4),
          ">",
          IntLiteralNode(6)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }

  test("... basic test with > between int and float") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          FloatLiteralNode(6.433),
          ">",
          IntLiteralNode(4)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic test with > of equal floats") {
    val ast = wrapSimpleAST(
      List(
        BinaryOpNode(
          FloatLiteralNode(6.433),
          ">",
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToBoolean.get === false)
  }


}
