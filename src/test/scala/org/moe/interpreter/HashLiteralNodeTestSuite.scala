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

  test("... basic test with Hash of variable values") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode("$foo", IntLiteralNode(10)),
        VariableDeclarationNode("$bar", IntLiteralNode(20)),
        HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), VariableAccessNode("$foo")),
            PairLiteralNode(StringLiteralNode("bar"), VariableAccessNode("$bar"))
          )
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)

    val hash: Map[String, MoeObject] = result.asInstanceOf[MoeHashObject].getNativeValue
    assert(hash("foo").asInstanceOf[MoeIntObject].getNativeValue === 10)
    assert(hash("bar").asInstanceOf[MoeIntObject].getNativeValue === 20)
  }

  test("... basic test with accessed hash values") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode("$foo", IntLiteralNode(10)),
        VariableDeclarationNode("$bar", IntLiteralNode(20)),
        VariableDeclarationNode(
          "%hash",
          HashLiteralNode(
            List(
              PairLiteralNode(StringLiteralNode("foo"), VariableAccessNode("$foo")),
              PairLiteralNode(StringLiteralNode("bar"), VariableAccessNode("$bar"))
            )
          )
        ),
        HashValueAccessNode(
          "%hash",
          StringLiteralNode("foo")
        )
      )
    )
    val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 10)
  }
}
