package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArrayMethodTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... basic test with array.map") {
    val result = interpretCode("[1, 2, 3].map(-> ($_) { $_ + 1 })")

    val array = result.unboxToArrayBuffer.get
    array.length should equal (3)
    array(0).unboxToInt.get should equal (2)
    array(1).unboxToInt.get should equal (3)
    array(2).unboxToInt.get should equal (4)
  }

  test("... basic test with array.grep") {
    val result = interpretCode("[1, 2, 3].grep(-> ($_) { $_ <= 2 })")

    val array = result.unboxToArrayBuffer.get
    array.length should equal (2)
    array(0).unboxToInt.get should equal (1)
    array(1).unboxToInt.get should equal (2)
  }

  test("... basic test with array.each") {
    val result = interpretCode("my @x = ['foo', 'bar', 'baz']; @x.each(-> ($_) { $_.chop }); @x")

    val array = result.unboxToArrayBuffer.get
    array.length should equal (3)
    array(0).unboxToString.get should equal ("fo")
    array(1).unboxToString.get should equal ("ba")
    array(1).unboxToString.get should equal ("ba")
  }

  test("... basic test with array.eqv (equal)") {
    val result = interpretCode("my @a1 = ['a', 'b', 1, 2]; my @a2 = ['a', 'b', 1, 2]; @a1.eqv(@a2)")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with array.eqv (not-equal)") {
    val result = interpretCode("my @a1 = ['a', 'b', 1, 2]; my @a2 = ['a', 'b', 1, '2']; @a1.eqv(@a2)")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic test with array.eqv (nested arrays - equal)") {
    val result = interpretCode("my @a1 = ['a', ['b', 1], 2]; my @a2 = ['a', ['b', 1], 2]; @a1.eqv(@a2)")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with array.eqv (nested arrays - not equal)") {
    val result = interpretCode("my @a1 = ['a', ['b', 1], 2]; my @a2 = ['a', ['b', 1, 2]]; @a1.eqv(@a2)")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic test with array.first") {
    val result = interpretCode("my @a = ['foo', 'bar', 'baz']; @a.first(-> ($_) { $_ lt 'foo' } )")
    result.unboxToString.get should equal ("bar")
  }

  test("... basic test with array.max") {
    val result = interpretCode("my @a = [3, 12, 9]; @a.max")
    result.unboxToInt.get should equal (12)
  }

  test("... basic test with array.maxstr") {
    val result = interpretCode("my @a = ['foo', 'bar', 'baz']; @a.maxstr")
    result.unboxToString.get should equal ("foo")
  }

  test("... basic test with array.min") {
    val result = interpretCode("my @a = [3, 12, 9]; @a.min")
    result.unboxToInt.get should equal (3)
  }

  test("... basic test with array.minstr") {
    val result = interpretCode("my @a = ['foo', 'bar', 'baz']; @a.minstr")
    result.unboxToString.get should equal ("bar")
  }
}
