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
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

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
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    val array: List[ MoeObject ] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 2)
    assert(array(0).asInstanceOf[ MoeIntObject ].getNativeValue === 10)

    val nested = array(1).asInstanceOf[ MoeArrayObject ].getNativeValue

    assert(nested.size === 1)
    assert(nested(0).asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }

  test("... basic test with Array accessing element 0") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(0))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeIntObject ].getNativeValue === 10)
  }

  test("... basic test with Array accessing element 1") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(1))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }

  test("... basic test with Array accessing last element with -1") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(-1))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }

  test("... basic test with Array accessing last element with -2") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(-2))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeIntObject ].getNativeValue === 10)
  }

  test("... basic test with Array accessing last element with -3") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(-3))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }

  test("... basic test with Array accessing last out of bounds") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "@array",
          ArrayLiteralNode(
            List(
              IntLiteralNode(10),
              IntLiteralNode(20)
            )
          )
        ),
        ArrayElementAccessNode("@array", IntLiteralNode(2))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)

    assert(result.asInstanceOf[ MoeUndefObject ].getNativeValue === null)
  }
}
