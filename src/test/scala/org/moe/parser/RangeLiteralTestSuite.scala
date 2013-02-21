package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class RangeLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with a simple integer range") {
    val result = interpretCode("1 .. 3")

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToInt.get === 1)
    assert(array(1).unboxToInt.get === 2)
    assert(array(2).unboxToInt.get === 3)
  }

  test("... basic test with a simple string range #1") {
    val result = interpretCode(""" "1" .. "3" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "1")
    assert(array(1).unboxToString.get === "2")
    assert(array(2).unboxToString.get === "3")
  }

  test("... basic test with a simple string range #2") {
    val result = interpretCode(""" "01" .. "03" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "01")
    assert(array(1).unboxToString.get === "02")
    assert(array(2).unboxToString.get === "03")
  }

  test("... basic test with a simple string range #3") {
    val result = interpretCode(""" "1" .. "3" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "1")
    assert(array(1).unboxToString.get === "2")
    assert(array(2).unboxToString.get === "3")
  }

  test("... basic test with a simple string range #4") {
    val result = interpretCode(""" "a" .. "c" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "a")
    assert(array(1).unboxToString.get === "b")
    assert(array(2).unboxToString.get === "c")
  }

  test("... basic test with a simple string range #5") {
    val result = interpretCode(""" "z" .. "ab" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 3)
    assert(array(0).unboxToString.get === "z")
    assert(array(1).unboxToString.get === "aa")
    assert(array(2).unboxToString.get === "ab")
  }

  test("... basic test with a simple string range #6") {
    val result = interpretCode(""" "ab" .. "z" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 0)
  }

  test("... basic test with a simple string range #7") {
    val result = interpretCode(""" "zz" .. "a" """)

    val array: List[MoeObject] = result.unboxToList.get

    assert(array.size === 0)
  }

}
