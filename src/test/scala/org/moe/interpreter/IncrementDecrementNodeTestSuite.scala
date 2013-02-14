package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class IncrementDecrementNodeTestSuite extends FunSuite with InterpreterTestUtils {

// integer post-increment tests
  test("... basic test with variable post-increment") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        IncrementNode(VariableAccessNode("$foo")),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 43)
  }

  test("... variable post-increment return value") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 42)
  }

// integer pre-increment tests
  test("... basic test with variable pre-increment") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        IncrementNode(VariableAccessNode("$foo"), true),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 43)
  }

  test("... variable pre-increment return value") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        IncrementNode(VariableAccessNode("$foo"),true)
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 43)
  }

// float post-increment tests
  test("... basic test with variable post-increment (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        IncrementNode(VariableAccessNode("$foo")),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 99.6)
  }

  test("... variable post-increment return value (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 98.6)
  }

// float pre-increment tests
  test("... basic test with variable pre-increment (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        IncrementNode(VariableAccessNode("$foo"), true),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 99.6)
  }

  test("... variable pre-increment return value (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        IncrementNode(VariableAccessNode("$foo"), true)
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 99.6)
  }

// integer post-decrement tests
  test("... basic test with variable post-decrement") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        DecrementNode(VariableAccessNode("$foo")),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 41)
  }

  test("... variable post-decrement return value") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        DecrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 42)
  }

// integer pre-decrement tests
  test("... basic test with variable pre-decrement") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        DecrementNode(VariableAccessNode("$foo"), true),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 41)
  }

  test("... variable pre-decrement return value") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(42)
        ),
        DecrementNode(VariableAccessNode("$foo"),true)
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 41)
  }

// float post-decrement tests
  test("... basic test with variable post-decrement (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        DecrementNode(VariableAccessNode("$foo")),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 97.6)
  }

  test("... variable post-decrement return value (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        DecrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 98.6)
  }

// float pre-decrement tests
  test("... basic test with variable pre-decrement (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        DecrementNode(VariableAccessNode("$foo"), true),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 97.6)
  }

  test("... variable pre-decrement return value (float)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          FloatLiteralNode(98.6)
        ),
        DecrementNode(VariableAccessNode("$foo"), true)
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 97.6)
  }

  test("... basic test with variable increment (string)") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("123")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "124")
  }

  test("... basic test with variable increment (string) #2") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("99")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "100")
  }

  test("... basic test with variable increment (string) #3") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("99")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "100")
  }

  test("... basic test with variable increment (string) #4") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("a0")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "a1")
  }

  test("... basic test with variable increment (string) #5") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("Az")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "Ba")
  }

  test("... basic test with variable increment (string) #6") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("zz")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "aaa")
  }

  test("... basic test with variable increment (string) #7") {
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          StringLiteralNode("01")
        ),
        IncrementNode(VariableAccessNode("$foo"))
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "02")
  }

}
