package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class MiscNodeTestSuite extends FunSuite with BeforeAndAfter with InterpreterTestUtils {

  private case class FooNode() extends AST

  test("... basic test that last value is the one returned") {
    val ast = wrapSimpleAST(
      List(
        StringLiteralNode("HELLO"),
        StringLiteralNode("WORLD")
      )
    )
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "WORLD")
  }

  test("... unknown node") {
    val ast = wrapSimpleAST(
      List(
        FooNode()
      )
    )
    intercept[MoeRuntime.Errors.UnknownNode] {
      Interpreter.eval(MoeRuntime.getRootEnv, ast)
    }
  }


}
