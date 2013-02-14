package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class MiscNodeTestSuite extends FunSuite with InterpreterTestUtils {

  private case class FooNode() extends AST

  test("... basic test that last value is the one returned") {
    val ast = wrapSimpleAST(
      List(
        StringLiteralNode("HELLO"),
        StringLiteralNode("WORLD")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "WORLD")
  }

  test("... unknown node") {
    val ast = wrapSimpleAST(
      List(
        FooNode()
      )
    )
    intercept[MoeErrors.UnknownNode] {
      interpreter.eval(runtime, runtime.getRootEnv, ast)
    }
  }


}
