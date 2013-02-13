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
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 100)
  }

  test("... a weird looking if") {
    val result = interpretCode("if(true) { 2; 7 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 7)
  }

  test("... nested if") {
    val result = interpretCode("if (true) { if ( true ) { 200 } }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... nested if true in if false ") {
    val result = interpretCode("if (false) { if ( true ) { 200 } }")
    assert(result.asInstanceOf[MoeUndefObject] === runtime.NativeObjects.getUndef)
  }

  test("... nested if false in if true") {
    val result = interpretCode("if (true) { if ( false ) { 200 } }")
    assert(result.asInstanceOf[MoeUndefObject] === runtime.NativeObjects.getUndef)
  }

  test("... nested if false in if false ") {
        val result = interpretCode("if (false) { if ( true ) { 200 } }")
        assert(result.asInstanceOf[MoeUndefObject] === runtime.NativeObjects.getUndef)
      }

}
