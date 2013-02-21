package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class IntLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... basic test with an 0 int") {
    val result = interpretCode("0")
    result.unboxToInt.get should equal (0)
  }

  test("... basic test with an -0 int") {
    val result = interpretCode("-0")
    result.unboxToInt.get should equal (0)
  }


  test("... basic test with an int") {
    val result = interpretCode("123")
    result.unboxToInt.get should equal (123)
  }

  test("... basic test with a negative int") {
    val result = interpretCode("-123")
    result.unboxToInt.get should equal (-123)
  }

  test("... basic test with an int - embedded underscore") {
    val result = interpretCode("123_456")
    result.unboxToInt.get should equal (123456)
  }

  test("... basic test with an octal int") {
    val result = interpretCode("0123")
    result.unboxToInt.get should equal (83)
  }

  test("... basic test with an octal int - embedded underscore") {
    val result = interpretCode("0123_456")
    result.unboxToInt.get should equal (42798)
  }

  test("... basic test with an hexadecimal int") {
    val result = interpretCode("0x123")
    result.unboxToInt.get should equal (0x123)
  }

  test("... basic test with an hexadecimal int - 2") {
    val result = interpretCode("0xFFFF")
    result.unboxToInt.get should equal (0xFFFF)
  }

  test("... basic test with an hexadecimal int - embedded underscore") {
    val result = interpretCode("0x123_456")
    result.unboxToInt.get should equal (0x123456)
  }

  test("... basic test with an hexadecimal int - embedded underscore - 2") {
    val result = interpretCode("0xAB_CDEF")
    result.unboxToInt.get should equal (0xABCDEF)
  }

  test("... basic test with an binary int literal") {
    val result = interpretCode("0b10110")
    result.unboxToInt.get should equal (22)
  }

  test("... basic test with an binary int literal - embedded underscore") {
    val result = interpretCode("0b1011_0110")
    result.unboxToInt.get should equal (182)
  }

}
