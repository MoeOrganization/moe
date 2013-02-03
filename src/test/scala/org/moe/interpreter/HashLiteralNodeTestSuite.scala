package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class HashLiteralNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with Hash") {
    val ast = wrapSimpleAST(
      List(
        HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)

    val hash: Map[String, MoeObject] = result.asInstanceOf[MoeHashObject].getNativeValue
    assert(hash("foo").asInstanceOf[MoeIntObject].getNativeValue === 10)
    assert(hash("bar").asInstanceOf[MoeIntObject].getNativeValue === 20)
  }

}
