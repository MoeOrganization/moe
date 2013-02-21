package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... literal int addition") {
    val result = interpretCode("2+2")
    result.unboxToInt.get should equal (4)
  }

  test("... literal int multiplication") {
    val result = interpretCode("3*3")
    result.unboxToInt.get should equal (9)
  }

  test("... order of operations, addition first") {
    val result = interpretCode("2+3*4")
    result.unboxToInt.get should equal (14)
  }

  test("... order of operations, multiplication first") {
    val result = interpretCode("2*3+4")
    result.unboxToInt.get should equal (10)
  }

}
