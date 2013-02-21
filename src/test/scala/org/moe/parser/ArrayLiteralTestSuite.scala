package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArrayLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with a one-element arrayref") {
    val result = interpretCode("[42]")

    val listElement = result.unboxToArray.get(0)
    assert(listElement.unboxToInt.get === 42);
  }

  test("... basic test with a four-element arrayref") {
    val result = interpretCode("""[42, 'jason', "may", true]""")
    val elems = result.unboxToArray.get

    assert(elems(0).unboxToInt.get === 42);

    assert(elems(1).unboxToString.get === "jason");
    assert(elems(2).unboxToString.get === "may");
    assert(elems(3).unboxToBoolean.get === true);
  }

  test("... basic test with a nested arrayref") {
    val result = interpretCode("""[42, ['jason', "may"], true]""")
    val elems = result.unboxToArray.get

    assert(elems(0).unboxToInt.get === 42);

    val nested_elems = elems(1).unboxToArray.get
    assert(nested_elems(0).unboxToString.get === "jason");
    assert(nested_elems(1).unboxToString.get === "may");

    assert(elems(2).unboxToBoolean.get === true);
  }

  test("... basic test with a nested hashref") {
    val result = interpretCode("""[42, {'jason' => "may"}, false]""")
    val elems = result.unboxToArray.get

    assert(elems(0).unboxToInt.get === 42);

    val nested_hash = elems(1).unboxToHash.get
    val key = nested_hash("jason")
    assert(key.unboxToString.get === "may")
    assert(elems(2).unboxToBoolean.get === false);
  }

  test("... basic test with arrayref containing a right-trailing list") {
    val result = interpretCode("[42, true, ]")
    val elems = result.unboxToArray.get

    assert(elems(0).unboxToInt.get === 42);
    assert(elems(1).unboxToBoolean.get === true);
  }

  test("... basic test with arrayref containing a left-trailing list") {
    val result = interpretCode("[, 42, true]")
    val elems = result.unboxToArray.get

    assert(elems(0).unboxToInt.get === 42);
    assert(elems(1).unboxToBoolean.get === true);
  }


}
