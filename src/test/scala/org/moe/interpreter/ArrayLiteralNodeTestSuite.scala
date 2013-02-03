package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import org.scalatest.FunSuite

class ArrayLiteralNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Array") {
    val ast = wrapSimpleAST(
      List(
        ArrayLiteralNode(
          List(
            IntLiteralNode(10),
            IntLiteralNode(20)
          )
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 2)
    assert(array(0).asInstanceOf[MoeIntObject].getNativeValue === 10)
    assert(array(1).asInstanceOf[MoeIntObject].getNativeValue === 20)
  }

  test("... complex test with Array") {
    val ast = wrapSimpleAST(
      List(
        ArrayLiteralNode(
          List(
            IntLiteralNode(10),
            ArrayLiteralNode(
              List(
                IntLiteralNode(20)
              )
            )
          )
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array: List[ MoeObject ] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 2)
    assert(array(0).asInstanceOf[ MoeIntObject ].getNativeValue === 10)

    val nested = array(1).asInstanceOf[ MoeArrayObject ].getNativeValue

    assert(nested.size === 1)
    assert(nested(0).asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }


}
