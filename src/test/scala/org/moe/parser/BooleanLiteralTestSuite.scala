package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class BooleanLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with a true") {
    val result = interpretCode("true")
    assert(result.unboxToBoolean.get === true)
  }

  test("... basic test with a false") {
    val result = interpretCode("false")
    assert(result.unboxToBoolean.get === false)
  }
  test("... basic test with a not true") {
    val result = interpretCode(" false ")
    assert(result.unboxToBoolean.get === false)
  }

}
