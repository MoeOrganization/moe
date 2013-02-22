package org.moe.interpreter

import org.scalatest.FunSuite

import scala.collection.mutable.HashMap
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
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val hash: HashMap[String, MoeObject] = result.unboxToMap.get
    assert(hash("foo").unboxToInt.get === 10)
    assert(hash("bar").unboxToInt.get === 20)
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
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val hash: HashMap[String, MoeObject] = result.unboxToMap.get
    assert(hash("foo").unboxToInt.get === 10)
    assert(hash("bar").unboxToInt.get === 20)
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
        HashElementAccessNode(
          "%hash",
          StringLiteralNode("foo")
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 10)
  }
}
