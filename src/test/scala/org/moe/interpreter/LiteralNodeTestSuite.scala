package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class LiteralNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with null") {
    val ast = wrapSimpleAST(List(UndefLiteralNode()))
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result === runtime.NativeObjects.getUndef)
  }

  test("... basic test with Int") {
    val ast = wrapSimpleAST(List(IntLiteralNode(10)))
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 10)
  }

  test("... basic test with Float") {
    val ast = wrapSimpleAST(List(FloatLiteralNode(10.5)))
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 10.5)
  }

  test("... basic test with String") {
    val ast = wrapSimpleAST(List(StringLiteralNode("HELLO")))
    val result = Interpreter.eval(runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "HELLO")
  }

}
