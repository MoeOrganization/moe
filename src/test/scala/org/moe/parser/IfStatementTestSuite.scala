package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class IfStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a simple if") {
    val result = interpretCode("if (true) { 100 }")
    assert(result.unboxToInt.get === 100)
  }

  test("... a weird looking if") {
    val result = interpretCode("if(true) { 2; 7 }")
    assert(result.unboxToInt.get === 7)
  }

  test("... nested if") {
    val result = interpretCode("if (true) { if ( true ) { 200 } }")
    assert(result.unboxToInt.get === 200)
  }

  test("... nested if true in if false ") {
    val result = interpretCode("if (false) { if ( true ) { 200 } }")
    assert(result.isUndef)
  }

  test("... nested if false in if true") {
    val result = interpretCode("if (true) { if ( false ) { 200 } }")
    assert(result.isUndef)
  }

  test("... nested if false in if false ") {
    val result = interpretCode("if (false) { if ( true ) { 200 } }")
    assert(result.isUndef)
  }

  test("... single else ") {
    val result = interpretCode("if (false) { 10 } else { 20 }")
    assert(result.unboxToInt.get === 20)
  }

  test("... nested single else ") {
    val result = interpretCode("if (false) { 10 } else { if (false) { 10 } else { 20 } }")
    assert(result.unboxToInt.get === 20)
  }

  test("... single elsif ") {
    val result = interpretCode("if (false) { 10 } elsif (true) { 20 }")
    assert(result.unboxToInt.get === 20)
  }

  test("... nested single elsif ") {
    val result = interpretCode("if (false) { 10 } elsif (true) { if (false) { 10 } elsif (true) { 20 } }")
    assert(result.unboxToInt.get === 20)
  }

  test("... double elsif ") {
    val result = interpretCode("if (false) { 10 } elsif (false) { 20 } elsif (true) { 30 }")
    assert(result.unboxToInt.get === 30)
  }

  test("... triple elsif ") {
    val result = interpretCode("if (false) { 10 } elsif (false) { 20 } elsif (false) { 30 } elsif (true) { 40 }")
    assert(result.unboxToInt.get === 40)
  }

  test("... double elsif with else") {
    val result = interpretCode("if (false) { 10 } elsif (false) { 20 } elsif (false) { 30 } else { 40 }")
    assert(result.unboxToInt.get === 40)
  }

}
