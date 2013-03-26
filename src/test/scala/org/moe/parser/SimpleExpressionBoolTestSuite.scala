package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionBoolTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... literal bool stringification ... [3 > 1 ==> true]") {
    val result = interpretCode("3 > 1")
    result.unboxToString.get should equal ("true")
  }

  test("... literal bool stringification ... [3 == 1 ==> false]") {
    val result = interpretCode("3 == 1")
    result.unboxToString.get should equal ("false")
  }

  test("... literal bool numification ... [3 > 1 ==> 1]") {
    val result = interpretCode("3 > 1")
    result.unboxToInt.get should equal (1)
  }

  test("... literal bool numification ... [3 == 1 ==> 0]") {
    val result = interpretCode("3 == 1")
    result.unboxToInt.get should equal (0)
  }

  // not

  test("... literal bool -- not ... [!(3 == 1) ==> true]") {
    val result = interpretCode("!(3 == 1)")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal bool -- not ... [!(3 != 1) ==> false]") {
    val result = interpretCode("!(3 != 1)")
    result.unboxToBoolean.get should equal (false)
  }

  // or

  test("... literal bool -- or [false || true ==> true] ... [3 == 1 || 3 == 3]") {
    val result = interpretCode("3 == 1 || 3 == 3")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal bool -- or [true || false ==> true] ... [3 != 1 || 2 == 1]") {
    val result = interpretCode("3 != 1 || 2 == 1 ")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal bool -- or [true || true ==> true] ... [3 != 1 || 2 != 1]") {
    val result = interpretCode("3 != 1 || 2 != 1 ")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal bool -- or [false || false ==> false] ... [3 == 1 || 2 == 1]") {
    val result = interpretCode("3 == 1 || 2 == 1 ")
    result.unboxToBoolean.get should equal (false)
  }

  // and

  test("... literal bool -- and [false && true ==> false] ... [3 == 1 && 3 == 3]") {
    val result = interpretCode("3 == 1 && 3 == 3")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal bool -- and [true && false ==> false] ... [3 != 1 && 2 == 1") {
    val result = interpretCode("3 != 1 && 2 == 1 ")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal bool -- and [true && true ==> true] ... [3 != 1 && 2 != 1]") {
    val result = interpretCode("3 != 1 && 2 != 1 ")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal bool -- and [false && false ==> false] ... [3 == 1 && 2 == 1]") {
    val result = interpretCode("3 == 1 && 2 == 1 ")
    result.unboxToBoolean.get should equal (false)
  }

  // logical or/and

  test("... logical or ... [10 || 42]") {
    val result = interpretCode("10 || 42")
    result.unboxToInt.get should equal (10)
  }

  test("... logical or ... [0 || 42]") {
    val result = interpretCode("0 || 42")
    result.unboxToInt.get should equal (42)
  }

  test("... logical and ... [10 && 42]") {
    val result = interpretCode("10 && 42")
    result.unboxToInt.get should equal (42)
  }

  test("... logical or ... [42 && 0]") {
    val result = interpretCode("42 && 0")
    result.unboxToInt.get should equal (0)
  }

  // ternary operator

  test("... ternary operator ... [1 == 1 ? 2 : 3]") {
    val result = interpretCode("1 == 1 ? 2 : 3")
    result.unboxToInt.get should equal (2)
  }

  test("... ternary operator ... [1 == 2 ? 2 : 3]") {
    val result = interpretCode("1 == 2 ? 2 : 3")
    result.unboxToInt.get should equal (3)
  }

  test("... ternary operator (right assoc) ... [1 == 1 ? 2 == 2 ? 3 : 4 : 5]") {
    val result = interpretCode("1 == 1 ? 2 == 2 ? 3 : 4 : 5")
    result.unboxToInt.get should equal (3)
  }

  test("... ternary operator (right assoc) ... [1 == 1 ? 2 != 2 ? 3 : 4 : 5]") {
    val result = interpretCode("1 == 1 ? 2 != 2 ? 3 : 4 : 5")
    result.unboxToInt.get should equal (4)
  }

  test("... ternary operator (right assoc) ... [1 != 1 ? 2 == 2 ? 3 : 4 : 5]") {
    val result = interpretCode("1 != 1 ? 2 == 2 ? 3 : 4 : 5")
    result.unboxToInt.get should equal (5)
  }

  test("... ternary operator (right assoc) ... [1 != 1 ? 2 : 3 == 3 ? 4 : 5]") {
    val result = interpretCode("1 != 1 ? 2 : 3 == 3 ? 4 : 5")
    result.unboxToInt.get should equal (4)
  }

  test("... ternary operator (right assoc) ... [1 != 1 ? 2 : 3 != 3 ? 4 : 5]") {
    val result = interpretCode("1 != 1 ? 2 : 3 != 3 ? 4 : 5")
    result.unboxToInt.get should equal (5)
  }
}
