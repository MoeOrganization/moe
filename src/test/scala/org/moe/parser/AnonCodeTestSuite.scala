package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class AnonCodeTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... a basic anon-sub") {
    val result = interpretCode("my &id = -> ($x) { $x }; &id->(10)")
    result.unboxToInt.get should equal (10)
  }

  test("... a basic anon-sub w/ apply") {
    val result = interpretCode("my &id = -> ($x) { $x }; &id->apply([10])")
    result.unboxToInt.get should equal (10)
  }

  test("... a complex anon-sub") {
    val result = interpretCode("my &adder = -> ($x, $y) { $x + $y }; &adder->(10, 20)")
    result.unboxToInt.get should equal (30)
  }

  test("... a complex anon-sub w/ apply") {
    val result = interpretCode("my &adder = -> ($x, $y) { $x + $y }; &adder->apply([10, 20])")
    result.unboxToInt.get should equal (30)
  }

  test("... a complex anon-sub w/out signature") {
    val result = interpretCode("my &adder = -> { @_[0] + @_[1] }; &adder->(10, 20)")
    result.unboxToInt.get should equal (30)
  }

  test("... a complex anon-sub w/out signature w/ apply") {
    val result = interpretCode("my &adder = -> { @_[0] + @_[1] }; &adder->apply([10, 20])")
    result.unboxToInt.get should equal (30)
  }

}
