package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class IntLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

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

}
