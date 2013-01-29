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

    val listElement = result.asInstanceOf[MoeArrayObject].getNativeValue(0)
    assert(listElement.asInstanceOf[MoeIntObject].getNativeValue === 42);
  }

  test("... basic test with a four-element arrayref") {
    val result = interpretCode("""[42, 'jason', "may", true]""")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    assert(elems(1).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(elems(2).asInstanceOf[MoeStringObject].getNativeValue === "may");
    assert(elems(3).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with nested arrayrefs") {
    val result = interpretCode("""[42, ['jason', "may"], true]""")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);

    val nested_elems = elems(1).asInstanceOf[MoeArrayObject].getNativeValue
    assert(nested_elems(0).asInstanceOf[MoeStringObject].getNativeValue === "jason");
    assert(nested_elems(1).asInstanceOf[MoeStringObject].getNativeValue === "may");

    assert(elems(2).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a right-trailing list") {
    val result = interpretCode("[42, true, ]")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }

  test("... basic test with arrayref containing a left-trailing list") {
    val result = interpretCode("[, 42, true]")
    val elems = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(elems(0).asInstanceOf[MoeIntObject].getNativeValue === 42);
    assert(elems(1).asInstanceOf[MoeBooleanObject].getNativeValue === true);
  }


}
