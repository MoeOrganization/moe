package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class IfNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with If") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(true),
            IntLiteralNode(1)
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 1)
  }

  test("... basic (false) test with If") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(1)
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result === runtime.NativeObjects.getUndef)
  }

  test("... basic test with If () Else") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(true),
            IntLiteralNode(2), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(3)))
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 2)
  }

  test("... basic (false) test with If () Else") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(2), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(3)))
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 3)
  }

  test("... basic (true/true) test with IfElsif (true/true)") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(true),
            IntLiteralNode(5), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(8)))
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 5)
  }

  test("... basic (false/true) test with IfElsif") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(5), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(8)))
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 8)
  }

  test("... basic (false/false) test with IfElseIf") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(5), 
            Some(new IfStruct(BooleanLiteralNode(false), IntLiteralNode(8)))
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result === runtime.NativeObjects.getUndef)
  }

  test("... basic (true/true) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(true),
            IntLiteralNode(34), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(55),
              Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(89))))
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 34)
  }

  test("... basic (false/true) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(34), 
            Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(55),
              Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(89))))
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 55)
  }

  test("... basic (false/false) test with IfElsifElse") {
    val ast = wrapSimpleAST(
      List(
        IfNode(
          new IfStruct(
            BooleanLiteralNode(false),
            IntLiteralNode(34), 
            Some(new IfStruct(BooleanLiteralNode(false), IntLiteralNode(55),
              Some(new IfStruct(BooleanLiteralNode(true), IntLiteralNode(89))))
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 89)
  }

}
