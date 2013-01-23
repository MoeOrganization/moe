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

  private def interpretCode(code: String): MoeObject = {
    val ast = basicAST(List(Parser.parseStuff(code)))
    Interpreter.eval(Runtime.getRootEnv, ast)
  }

  test("... basic test with an int") {
    val result = interpretCode("123")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 123)
  }

  test("... basic test with an int - embedded underscore") {
    val result = interpretCode("123_456")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 123456)
  }

  test("... basic test with an octal int") {
    val result = interpretCode("0123")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 83)
  }

  test("... basic test with an octal int - embedded underscore") {
    val result = interpretCode("0123_456")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 42798)
  }

  test("... basic test with an hexadecimal int") {
    val result = interpretCode("0x123")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0x123)
  }

  test("... basic test with an hexadecimal int - 2") {
    val result = interpretCode("0xFFFF")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0xFFFF)
  }

  test("... basic test with an hexadecimal int - embedded underscore") {
    val result = interpretCode("0x123_456")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0x123456)
  }

  test("... basic test with an hexadecimal int - embedded underscore - 2") {
    val result = interpretCode("0xAB_CDEF")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0xABCDEF)
  }

  test("... basic test with an binary int literal") {
    val result = interpretCode("0b10110")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 22)
  }

  test("... basic test with an binary int literal - embedded underscore") {
    val result = interpretCode("0b1011_0110")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 182)
  }

  test("... basic test with a float") {
    val result = interpretCode("123.5")
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 123.5)
  }

  test("... basic test with a float - 2") {
    val result = interpretCode(".5678")
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === .5678)
  }

  test("... basic test with a float - embedded underscore") {
    val result = interpretCode("123_456.56_789")
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 123456.56789)
  }

  test("... basic test with a true") {
    val result = interpretCode("true")
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with a false") {
    val result = interpretCode("false")
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with a simple double-quoted string") {
    val result = interpretCode("\"hello world\"")
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

  test("... basic test with a simple single-quoted string") {
    val result = interpretCode("'hello world'")
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

  test("... basic test with a one-element arrayref") {
    val result = interpretCode("[42]")

    val listElement = result.asInstanceOf[MoeArrayObject].getNativeValue(0)
    assert(listElement.asInstanceOf[MoeIntObject].getNativeValue === 42);
  }

  test("... basic test with a four-element arrayref") {
    val result = interpretCode("""[42, 'jason', "may", true]""")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    assert(elems(1).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(elems(2).asInstanceOf[MoeStringObject].getNativeValue === "may");
    assert(elems(3).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with nested arrayrefs") {
    val result = interpretCode("""[42, ['jason', "may"], true]""")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    val nested_elems = elems(1).asInstanceOf[MoeArrayObject].getNativeValue
    assert(nested_elems(0).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(nested_elems(1).asInstanceOf[MoeStringObject].getNativeValue === "may");

    assert(elems(2).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a right-trailing list") {
    val result = interpretCode("[42, true, ]")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a left-trailing list") {
    val result = interpretCode("[, 42, true]")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with hashref") {
    val result = interpretCode("{foo => 'bar'}")
    val map = result.asInstanceOf[MoeHashObject].getNativeValue
    val key = map("foo")
    assert(key.asInstanceOf[MoeStringObject].getNativeValue === "bar")
  }

  test("... a weird looking if") {
    val result = interpretCode("if(true) { 2; 7 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 7)
  }

  // TODO test environment stack via syntax when we are far enough
  test("... a do block") {
    val result = interpretCode("do { 100 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 100)
  }

  test("... a multi-statement do block") {
    val result = interpretCode("do { 100; 200 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... a do block with semicolons at the end") {
    val result = interpretCode("do { 100; 200; }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... a do block with multiple semicolons at the end") {
    val result = interpretCode("do { 100; 200;; }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... a basic try block") {
    val result = interpretCode("try { 100 }")
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch block") {
    val result = interpretCode("try { 100 } catch (Exception $e) { 'yup' }")
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch-catch block") {
    val result = interpretCode("try { 100 } " +
      "catch (Exception $e) { 'yup' }" +
      "catch (OtherException $e) { 'indeed' }"
    )
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch-catch-finally block") {
    val result = interpretCode("try { 100 } " +
      "catch (Exception $e) { 'yup' }" +
      "catch (Other::Exception $e) { 'indeed' }" +
      "finally { 200 }"
    )
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic package block") {
    val result = interpretCode("""
      package foo::bar {
      }
    """)
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }
}
