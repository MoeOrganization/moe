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

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeIntObject].getNativeValue === 1)
    assert(array(1).asInstanceOf[MoeIntObject].getNativeValue === 2)
    assert(array(2).asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

  test("... basic test with a simple string range #1") {
    val result = interpretCode(""" "1" .. "3" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeStringObject].getNativeValue === "1")
    assert(array(1).asInstanceOf[MoeStringObject].getNativeValue === "2")
    assert(array(2).asInstanceOf[MoeStringObject].getNativeValue === "3")
  }

  test("... basic test with a simple string range #2") {
    val result = interpretCode(""" "01" .. "03" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeStringObject].getNativeValue === "01")
    assert(array(1).asInstanceOf[MoeStringObject].getNativeValue === "02")
    assert(array(2).asInstanceOf[MoeStringObject].getNativeValue === "03")
  }

  test("... basic test with a simple string range #3") {
    val result = interpretCode(""" "1" .. "3" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeStringObject].getNativeValue === "1")
    assert(array(1).asInstanceOf[MoeStringObject].getNativeValue === "2")
    assert(array(2).asInstanceOf[MoeStringObject].getNativeValue === "3")
  }

  test("... basic test with a simple string range #4") {
    val result = interpretCode(""" "a" .. "c" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeStringObject].getNativeValue === "a")
    assert(array(1).asInstanceOf[MoeStringObject].getNativeValue === "b")
    assert(array(2).asInstanceOf[MoeStringObject].getNativeValue === "c")
  }

  test("... basic test with a simple string range #5") {
    val result = interpretCode(""" "z" .. "ab" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 3)
    assert(array(0).asInstanceOf[MoeStringObject].getNativeValue === "z")
    assert(array(1).asInstanceOf[MoeStringObject].getNativeValue === "aa")
    assert(array(2).asInstanceOf[MoeStringObject].getNativeValue === "ab")
  }

  test("... basic test with a simple string range #6") {
    val result = interpretCode(""" "ab" .. "z" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 0)
  }

  test("... basic test with a simple string range #7") {
    val result = interpretCode(""" "zz" .. "a" """)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 0)
  }

}
