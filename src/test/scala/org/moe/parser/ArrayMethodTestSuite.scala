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

  test("... basic test with array.reduce") {
    val result = interpretCode("my @x = [1, 2, 3, 4, 5]; @x.reduce(-> ($a, $b) { $a + $b })")
    result.unboxToInt.get should equal (15)
  }

  test("... basic test with array.reduce with initial value") {
    val result = interpretCode("my @x = [1, 2, 3, 4, 5]; @x.reduce(-> ($a, $b) { $a + $b }, 100)")
    result.unboxToInt.get should equal (115)
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

  test("... basic test with array.shuffle") {
    val result = interpretCode("my @a = [1, 2, 3, 4, 5]; @a.shuffle")
    val array = result.unboxToArrayBuffer.get
    array.length should equal (5)
    array.mkString("") should not equal ("12345")
  }

  test("... basic test with array.sum") {
    val result = interpretCode("my @a = [1, 2, 3, 4, 5]; @a.sum")
    result.unboxToInt.get should equal (15)
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

  test("... basic test with array.flatten") {
    val result = interpretCode("my @a = [[1, 1], 2, [3, [5, 8]]]; @a.flatten")
    val array = result.unboxToArrayBuffer.get
    array.length should equal (6)
    array.mkString(",") should equal ("1,1,2,3,5,8")
  }

  test("... basic test with array repetition") {
    val result = interpretCode("my @a = [1, 2, 3]; @a x 3")
    val array = result.unboxToArrayBuffer.get
    array.length should equal (9)
    array.mkString(",") should equal ("1,2,3,1,2,3,1,2,3")
  }

  test("... basic test with array join") {
    val result = interpretCode("""my @a = ["a", "b", "c"]; @a.join("|")""")
    result.unboxToString.get should equal ("a|b|c")
  }

  test("... basic test with array join -- empty separator") {
    val result = interpretCode("""my @a = ["a", "b", "c"]; @a.join("")""")
    result.unboxToString.get should equal ("abc")
  }

  test("... basic test with array join -- no separator") {
    val result = interpretCode("""my @a = ["a", "b", "c"]; @a.join""")
    result.unboxToString.get should equal ("abc")
  }

  test("... basic test with array.exists") {
    val result = interpretCode("""my @a = [2, 3, 4]; 1..5.map(-> ($x) { @a.exists($x) } )""")
    val array = result.unboxToArrayBuffer.get
    array.length should equal (5)
    array(0).unboxToBoolean.get should equal (false)
    array(1).unboxToBoolean.get should equal (true)
    array(2).unboxToBoolean.get should equal (true)
    array(3).unboxToBoolean.get should equal (true)
    array(4).unboxToBoolean.get should equal (false)
  }

  test("... basic test with array.uniq") {
    val result = interpretCode("""my @a = [1, 1, 2, 3, 4, 5, 4]; @a.uniq.join(",")""")
    result.unboxToString.get should equal ("1,2,3,4,5")
  }

  test("... basic test with array.zip") {
    val result = interpretCode("""my @a = ["a", "b", "c"]; my @b = 1..3; @a.zip(@b).flatten.join""")
    result.unboxToString.get should equal ("a1b2c3")
  }
}
