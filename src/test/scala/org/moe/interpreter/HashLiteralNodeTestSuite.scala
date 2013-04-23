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
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val hash = result.unboxToMap.get
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

    val hash = result.unboxToMap.get
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
          List(StringLiteralNode("foo"))
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 10)
  }

  test("... basic test with calling at_key method on hash") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "%hash",
          HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
        ),
        MethodCallNode(
          VariableAccessNode("%hash"),
          "at_key",
          List(StringLiteralNode("foo"))
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 10)
  }

  test("... basic test with calling at_key method on hash (with miss)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "%hash",
          HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
        ),
        MethodCallNode(
          VariableAccessNode("%hash"),
          "at_key",
          List(StringLiteralNode("baz"))
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.isUndef)
  }

  test("... basic test with calling bind_key method on hash") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "%hash",
          HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
        ),
        MethodCallNode(
          VariableAccessNode("%hash"),
          "bind_key",
          List(StringLiteralNode("bar"), IntLiteralNode(30))
        ),
        HashElementAccessNode(
          "%hash",
          List(StringLiteralNode("bar"))
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 30)
  }

  test("... basic test with calling keys method on hash") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "%hash",
          HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
        ),
        MethodCallNode(
          VariableAccessNode("%hash"),
          "keys",
          List()
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    val keys   = result.unboxToArrayBuffer.get
    assert(keys(0).unboxToString.get === "foo")
    assert(keys(1).unboxToString.get === "bar")
  }

}
