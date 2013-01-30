package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class IfNodeTestSuite extends FunSuite with BeforeAndAfter with InterpreterTestUtils {

  test("... basic test with If") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          BooleanLiteralNode(true),
          IntLiteralNode(1)
        )
      )
    )
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 1)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result === MoeRuntime.NativeObjects.getUndef)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 5)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 8)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result === MoeRuntime.NativeObjects.getUndef)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 34)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 55)
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
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 89)
  }

}
