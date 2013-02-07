package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import org.scalatest.FunSuite

class RangeLiteralNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Range") {
    val ast = wrapSimpleAST(
      List(
        RangeLiteralNode(
          IntLiteralNode(1),
          IntLiteralNode(3)
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeIntObject].getNativeValue === 1)
    assert(array(1).asInstanceOf[MoeIntObject].getNativeValue === 2)
    assert(array(2).asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

}
