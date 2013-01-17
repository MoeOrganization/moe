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

  test("... basic test with an int - embedded underscore") {
    val ast = basicAST(List(Parser.parseStuff("123_456")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 123456)
  }

  test("... basic test with an octal int") {
    val ast = basicAST(List(Parser.parseStuff("0123")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 83)
  }

  test("... basic test with an octal int - embedded underscore") {
    val ast = basicAST(List(Parser.parseStuff("0123_456")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 42798)
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

  test("... basic test with an hexadecimal int - embedded underscore") {
    val ast = basicAST(List(Parser.parseStuff("0x123_456")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0x123456)
  }

  test("... basic test with an hexadecimal int - embedded underscore - 2") {
    val ast = basicAST(List(Parser.parseStuff("0xAB_CDEF")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0xABCDEF)
  }

  test("... basic test with an binary int literal") {
    val ast = basicAST(List(Parser.parseStuff("0b10110")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 22)
  }

  test("... basic test with an binary int literal - embedded underscore") {
    val ast = basicAST(List(Parser.parseStuff("0b1011_0110")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 182)
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

  test("... basic test with a float - embedded underscore") {
    val ast = basicAST(List(Parser.parseStuff("123_456.56_789")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 123456.56789)
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

  test("... basic test with a simple double-quoted string") {
    val ast = basicAST(List(Parser.parseStuff("\"hello world\"")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

  test("... basic test with a simple single-quoted string") {
    val ast = basicAST(List(Parser.parseStuff("'hello world'")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

  test("... basic test with a one-element arrayref") {
    val ast = basicAST(List(Parser.parseStuff("[42]")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)

    val listElement = result.asInstanceOf[MoeArrayObject].getNativeValue(0)
    assert(listElement.asInstanceOf[MoeIntObject].getNativeValue === 42);
  }

  test("... basic test with a four-element arrayref") {
    val ast = basicAST(List(Parser.parseStuff("""[42, 'jason', "may", true]""")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    assert(elems(1).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(elems(2).asInstanceOf[MoeStringObject].getNativeValue === "may");
    assert(elems(3).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with nested arrayrefs") {
    val ast = basicAST(List(Parser.parseStuff("""[42, ['jason', "may"], true]""")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    val nested_elems = elems(1).asInstanceOf[MoeArrayObject].getNativeValue
    assert(nested_elems(0).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(nested_elems(1).asInstanceOf[MoeStringObject].getNativeValue === "may");

    assert(elems(2).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a right-trailing list") {
    val ast = basicAST(List(Parser.parseStuff("[42, true, ]")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a left-trailing list") {
    val ast = basicAST(List(Parser.parseStuff("[, 42, true]")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }
}
