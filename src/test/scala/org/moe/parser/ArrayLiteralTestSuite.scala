package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArrayLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... basic test with a one-element arrayref") {
    val result = interpretCode("[42]")

    val listElement = result.unboxToArrayBuffer.get(0)
    listElement.unboxToInt.get should equal (42);
  }

  test("... basic test with a four-element arrayref") {
    val result = interpretCode("""[42, 'jason', "may", true]""")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (42);

    elems(1).unboxToString.get should equal ("jason");
    elems(2).unboxToString.get should equal ("may");
    elems(3).unboxToBoolean.get should equal (true);
  }

  test("... basic test with a nested arrayref") {
    val result = interpretCode("""[42, ['jason', "may"], true]""")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (42);

    val nested_elems = elems(1).unboxToArrayBuffer.get
    nested_elems(0).unboxToString.get should equal ("jason");
    nested_elems(1).unboxToString.get should equal ("may");

    elems(2).unboxToBoolean.get should equal (true);
  }

  test("... basic test with a nested hashref") {
    val result = interpretCode("""[42, {'jason' => "may"}, false]""")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (42);

    val nested_hash = elems(1).unboxToMap.get
    val key = nested_hash("jason")
    key.unboxToString.get should equal ("may")
    elems(2).unboxToBoolean.get should equal (false);
  }

  test("... basic test with arrayref containing a right-trailing list") {
    val result = interpretCode("[42, true, ]")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (42);
    elems(1).unboxToBoolean.get should equal (true);
  }

  test("... basic test with arrayref containing a left-trailing list") {
    val result = interpretCode("[, 42, true]")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (42);
    elems(1).unboxToBoolean.get should equal (true);
  }

  test("... basic test for array element assignment") {
    val result = interpretCode("my @a = [42, true]; @a[0] = @a[0] + 1; @a")
    val elems = result.unboxToArrayBuffer.get

    elems(0).unboxToInt.get should equal (43);
  }

  test("... basic test for array element assignment -- non-existent element") {
    val result = interpretCode("""my @a = [42, true]; @a[3] = "foo"; @a""")
    val elems = result.unboxToArrayBuffer.get

    elems.length should equal (4)
    elems(3).unboxToString.get should equal ("foo");
  }

  test("... basic test for nested array element access") {
    val result = interpretCode("""my @a = [42, ['jason', "may"], true]; @a[1][1]""")
    result.unboxToString.get should equal ("may");
  }

  test("... basic test for nested array element access -- non-existent element") {
    val result = interpretCode("""my @a = [42, ['jason', "may"], true]; @a[1][2]""")
    assert(result.isUndef);
  }

  test("... basic test for nested array element access -- three levels") {
    val result = interpretCode("""my @a = [1, 2, [3, 4, [5, 6, 7]]]; @a[2][2][2]""")
    result.unboxToInt.get should equal (7);
  }


  test("... basic test for nested array element assignment") {
    val result = interpretCode("""my @a = [42, ['jason', "may"], true]; @a[1][1] = "May"; @a[1][1]""")
    result.unboxToString.get should equal ("May");
  }

  test("... basic test for nested array element assignment -- non-existent element") {
    val result = interpretCode("""my @a = [42, ['jason', "may"], true]; @a[1][2] = "rocks"; @a[1][2]""")
    result.unboxToString.get should equal ("rocks");
  }

  test("... basic test for nested array element assignment -- three levels") {
    val result = interpretCode("""my @a = [1, 2, [3, 4, [5, 6, 7]]]; @a[2][2][2] = 42; @a[2][2][2]""")
    result.unboxToInt.get should equal (42);
  }

}
