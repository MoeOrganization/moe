package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ParserTestSuite extends FunSuite with BeforeAndAfter {

  private def basicAST(nodes: List[AST]) =
    CompilationUnitNode(ScopeNode(StatementsNode(nodes)))

  test("... basic test with an int") {
    val ast = basicAST(List(Parser.parseStuff("123")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 123)
  }

  test("... basic test with an octal int") {
    val ast = basicAST(List(Parser.parseStuff("0123")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 83)
  }

  test("... basic test with an hexadecimal int") {
    val ast = basicAST(List(Parser.parseStuff("0x123")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0x123)
  }

  test("... basic test with an hexadecimal int - 2") {
    val ast = basicAST(List(Parser.parseStuff("0xFFFF")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0xFFFF)
  }

  test("... basic test with an binary int literal") {
    val ast = basicAST(List(Parser.parseStuff("0b10110")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 22)
  }

  test("... basic test with a float") {
    val ast = basicAST(List(Parser.parseStuff("123.5")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 123.5)
  }

  test("... basic test with a float - 2") {
    val ast = basicAST(List(Parser.parseStuff(".5678")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === .5678)
  }

  test("... basic test with a true") {
    val ast = basicAST(List(Parser.parseStuff("true")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with a false") {
    val ast = basicAST(List(Parser.parseStuff("false")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

}
