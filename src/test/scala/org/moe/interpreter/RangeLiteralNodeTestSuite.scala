package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import org.scalatest.FunSuite

class RangeLiteralNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Integer Range") {
    val ast = wrapSimpleAST(
      List(
        RangeLiteralNode(
          IntLiteralNode(1),
          IntLiteralNode(3)
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array = result.unboxToArrayBuffer.get

    assert(array.size === 3)
    assert(array(0).unboxToInt.get === 1)
    assert(array(1).unboxToInt.get === 2)
    assert(array(2).unboxToInt.get === 3)
  }

  test("... basic test with String Range") {
    val ast = wrapSimpleAST(
      List(
        RangeLiteralNode(
          StringLiteralNode("a"),
          StringLiteralNode("c")
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array = result.unboxToArrayBuffer.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "a")
    assert(array(1).unboxToString.get === "b")
    assert(array(2).unboxToString.get === "c")
  }

}
