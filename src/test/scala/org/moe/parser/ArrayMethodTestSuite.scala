package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArrayMethodsTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with array->map") {
    val result = interpretCode("[1, 2, 3]->map(-> ($_) { $_ + 1 })")

    val array = result.unboxToArrayBuffer.get
    assert(array.length === 3)
    assert(array(0).unboxToInt.get === 2);
    assert(array(1).unboxToInt.get === 3);
    assert(array(2).unboxToInt.get === 4);
  }

  test("... basic test with array->grep") {
    val result = interpretCode("[1, 2, 3]->grep(-> ($_) { $_ <= 2 })")

    val array = result.unboxToArrayBuffer.get
    assert(array.length === 2)
    assert(array(0).unboxToInt.get === 1);
    assert(array(1).unboxToInt.get === 2);
  }

  test("... basic test with array->each") {
    val result = interpretCode("my @x = ['foo', 'bar', 'baz']; @x->each(-> ($_) { $_->chop }); @x")

    val array = result.unboxToArrayBuffer.get
    assert(array.length === 3)
    assert(array(0).unboxToString.get === "fo");
    assert(array(1).unboxToString.get === "ba");
    assert(array(1).unboxToString.get === "ba");
  }


}
