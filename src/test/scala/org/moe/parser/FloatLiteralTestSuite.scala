package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class FloatLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with a float") {
    val result = interpretCode("123.5")
    assert(result.unboxToDouble.get === 123.5)
  }

  test("... basic test with a negative float") {
    val result = interpretCode("-123.5")
    assert(result.unboxToDouble.get === -123.5)
  }

  test("... basic test with a positive float") {
    val result = interpretCode("+123.5")
    assert(result.unboxToDouble.get === 123.5)
  }

  test("... basic test with a float - 2") {
    val result = interpretCode(".5678")
    assert(result.unboxToDouble.get === .5678)
  }

  test("... basic test with a float - embedded underscore") {
    val result = interpretCode("123_456.56_789")
    assert(result.unboxToDouble.get === 123456.56789)
  }

  test("... basic test with a float - scientific notation") {
    val result = interpretCode(".5678e18")
    assert(result.unboxToDouble.get === 5.678e17)
  }

  test("... basic test with a float - scientific notation - positive exponent") {
    val result = interpretCode("0.5678e+18")
    assert(result.unboxToDouble.get === 5.678e17)
  }

  test("... basic test with a float - scientific notation - negative exponent") {
    val result = interpretCode("0.5678e-18")
    assert(result.unboxToDouble.get === 5.678e-19)
  }

}
