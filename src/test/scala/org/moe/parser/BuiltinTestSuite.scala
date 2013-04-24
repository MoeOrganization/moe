package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class BuiltinTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {
  test("... rand -- with no argument") {
    val result = interpretCode("rand()").unboxToDouble.get
    result should be >= (0.0)
    result should be <  (1.0)
  }

  test("... rand -- with a limit argument") {
    val result = interpretCode("rand(10)").unboxToDouble.get
    result should be >= (0.0)
    result should be <  (10.0)
  }
}
