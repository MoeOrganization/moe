package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class ComparisonNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with <") {
    val ast = wrapSimpleAST(
      List(
        LessThanNode(
          IntLiteralNode(4),
          IntLiteralNode(6)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic (false) test with <") {
    val ast = wrapSimpleAST(
      List(
        LessThanNode(
          IntLiteralNode(6),
          IntLiteralNode(4)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with < between int and float") {
    val ast = wrapSimpleAST(
      List(
        LessThanNode(
          IntLiteralNode(4),
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with < of equal floats") {
    val ast = wrapSimpleAST(
      List(
        LessThanNode(
          FloatLiteralNode(6.433),
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with >") {
    val ast = wrapSimpleAST(
      List(
        GreaterThanNode(
          IntLiteralNode(6),
          IntLiteralNode(4)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic (false) test with >") {
    val ast = wrapSimpleAST(
      List(
        GreaterThanNode(
          IntLiteralNode(4),
          IntLiteralNode(6)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with > between int and float") {
    val ast = wrapSimpleAST(
      List(
        GreaterThanNode(
          FloatLiteralNode(6.433),
          IntLiteralNode(4)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with > of equal floats") {
    val ast = wrapSimpleAST(
      List(
        GreaterThanNode(
          FloatLiteralNode(6.433),
          FloatLiteralNode(6.433)
        )
      )
    )
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }


}
