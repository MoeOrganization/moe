package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionNumTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  val delta = 2e-8

  // addition

  test("... literal float addition ... [2.3+2]") {
    val result = interpretCode("2.3+2")
    result.unboxToDouble.get should be (4.3 plusOrMinus delta)
  }

  test("... literal float addition ... [2.3+3.2]") {
    val result = interpretCode("2.3+3.2")
    result.unboxToDouble.get should be (5.5 plusOrMinus delta)
  }

  test("... literal float addition ... [-2.3+3.2]") {
    val result = interpretCode("-2.3+3.2")
    result.unboxToDouble.get should be (0.9 plusOrMinus delta)
  }

  test("... literal float addition ... [2.3+-3.2]") {
    val result = interpretCode("2.3+-3.2")
    result.unboxToDouble.get should be (-0.9 plusOrMinus delta)
  }

  test("... literal float addition ... [2.3 + +3.2]") {
    val result = interpretCode("2.3 + +3.2")
    result.unboxToDouble.get should be (5.5 plusOrMinus delta)
  }

  test("... literal float addition ... [2.3e-2 + 3.2e2]") {
    val result = interpretCode("2.3e-2 + 3.2e2")
    result.unboxToDouble.get should be (320.023 plusOrMinus delta)
  }

  // subtraction

  test("... literal float subtraction ... [2.3-2]") {
    val result = interpretCode("2.3-2")
    result.unboxToDouble.get should be (0.3 plusOrMinus delta)
  }

  test("... literal float subtraction ... [2.3-3.2]") {
    val result = interpretCode("2.3-3.2")
    result.unboxToDouble.get should be (-0.9 plusOrMinus delta)
  }

  test("... literal float subtraction ... [-2.3-3.2]") {
    val result = interpretCode("-2.3-3.2")
    result.unboxToDouble.get should be (-5.5 plusOrMinus delta)
  }

  test("... literal float subtraction ... [2.3--3.2]") {
    val result = interpretCode("2.3--3.2")
    result.unboxToDouble.get should be (5.5 plusOrMinus delta)
  }

  test("... literal float subtraction ... [2.3 - +3.2]") {
    val result = interpretCode("2.3 - +3.2")
    result.unboxToDouble.get should be (-0.9 plusOrMinus delta)
  }

  test("... literal float subtraction ... [2.3e-2 - 3.2e2]") {
    val result = interpretCode("2.3e-2 - 3.2e2")
    result.unboxToDouble.get should be (-319.977 plusOrMinus delta)
  }

  // multiplication

  test("... literal float multiplication ... [2.3*2]") {
    val result = interpretCode("2.3*2")
    result.unboxToDouble.get should be (4.6 plusOrMinus delta)
  }

  // division

  test("... literal float division ... [2.3/2]") {
    val result = interpretCode("2.3/2")
    result.unboxToDouble.get should be (1.15 plusOrMinus delta)
  }

  // modulo

  test("... literal float modulo operation ... [13.0 % 4.0]") {
    val result = interpretCode("13.0 % 4.0")
    result.unboxToDouble.get should equal (1)
  }

  test("... literal float modulo operation ... [-13.0 % 4.0]") {
    val result = interpretCode("-13.0 % 4.0")
    result.unboxToDouble.get should equal (3)
  }

  test("... literal float modulo operation ... [13.0 % -4.0]") {
    val result = interpretCode("13.0 % -4.0")
    result.unboxToDouble.get should equal (-3)
  }

  test("... literal float modulo operation ... [-13.0 % -4.0]") {
    val result = interpretCode("-13.0 % -4.0")
    result.unboxToDouble.get should equal (-1)
  }

  // exponentiation

  test("... literal float exponentiation operation ... [2.2**2]") {
    val result = interpretCode("2.2**2")
    result.unboxToDouble.get should be (4.84 plusOrMinus 0.00000001)
  }

  test("... literal float exponentiation operation ... [2.2**2.2]") {
    val result = interpretCode("2.2**2.2")
    result.unboxToDouble.get should be (5.66669577 plusOrMinus 0.00000001)
  }

  // relational

  test("... literal float relational operation ... [1.1 < 2]") {
    val result = interpretCode("1.1 < 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.0 < 1]") {
    val result = interpretCode("2.0 < 1")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal float relational operation ... [2.1 < 2.5]") {
    val result = interpretCode("2.1 < 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.1 > 1]") {
    val result = interpretCode("2.1 > 1")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.0 > 3.5]") {
    val result = interpretCode("2.0 > 3.5")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal float relational operation ... [2.9 > 2.5]") {
    val result = interpretCode("2.9 > 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [1.1 <= 2]") {
    val result = interpretCode("1.1 <= 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [1.1 <= 1.1]") {
    val result = interpretCode("1.1 <= 1.1")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.0 <= 1]") {
    val result = interpretCode("2.0 <= 1")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal float relational operation ... [2.1 <= 2.5]") {
    val result = interpretCode("2.1 <= 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.1 >= 1]") {
    val result = interpretCode("2.1 >= 1")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.0 >= 3.5]") {
    val result = interpretCode("2.0 >= 3.5")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal float relational operation ... [2.9 >= 2.5]") {
    val result = interpretCode("2.9 >= 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.9 >= 2.9]") {
    val result = interpretCode("2.9 >= 2.9")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.9 == 2.9]") {
    val result = interpretCode("2.9 == 2.9")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal float relational operation ... [2.9 != 2.9]") {
    val result = interpretCode("2.9 != 2.9")
    result.unboxToBoolean.get should equal (false)
  }
}
