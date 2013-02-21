package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class IfNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with If") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          BooleanLiteralNode(true),
          IntLiteralNode(1)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 1)
  }

  test("... basic (false) test with If") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          BooleanLiteralNode(false),
          IntLiteralNode(1)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result === runtime.NativeObjects.getUndef)
  }

  test("... basic test with IfElse") {
    val ast = wrapSimpleAST(
      List(
        IfElseNode(
          BooleanLiteralNode(true),
          IntLiteralNode(2),
          IntLiteralNode(3)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 2)
  }

  test("... basic (false) test with IfElse") {
    val ast = wrapSimpleAST(
      List(
        IfElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(2),
          IntLiteralNode(3)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 3)
  }

  test("... basic (true/true) test with IfElsif (true/true)") {
    val ast = wrapSimpleAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(true),
          IntLiteralNode(5),
          BooleanLiteralNode(true),
          IntLiteralNode(8)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 5)
  }

  test("... basic (false/true) test with IfElsif") {
    val ast = wrapSimpleAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(false),
          IntLiteralNode(5),
          BooleanLiteralNode(true),
          IntLiteralNode(8)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 8)
  }

  test("... basic (false/false) test with IfElseIf") {
    val ast = wrapSimpleAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(false),
          IntLiteralNode(13),
          BooleanLiteralNode(false),
          IntLiteralNode(21)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result === runtime.NativeObjects.getUndef)
  }

  test("... basic (true/true) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(true),
          IntLiteralNode(34),
          BooleanLiteralNode(true),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 34)
  }

  test("... basic (false/true) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(34),
          BooleanLiteralNode(true),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 55)
  }

  test("... basic (false/false) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(34),
          BooleanLiteralNode(false),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 89)
  }

}
