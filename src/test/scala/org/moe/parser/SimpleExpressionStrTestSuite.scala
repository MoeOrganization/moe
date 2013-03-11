package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionStrTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... literal string concatenation ... [\"Hello \" . \"Moe\"]") {
    val result = interpretCode(""""Hello " . "Moe"""")
    result.unboxToString.get should equal ("Hello Moe")
  }

  test("... literal string concatenation ... ['foo' . 'bar']") {
    val result = interpretCode("'foo' . 'bar'")
    result.unboxToString.get should equal ("foobar")
  }

  test("... literal string concatenation ... ['1' . 2]") {
    val result = interpretCode("'1' . 2")
    result.unboxToString.get should equal ("12")
  }

  test("... literal string concatenation ... ['1' . 2 * 3]") {
    val result = interpretCode("'1' . 2 * 3")
    result.unboxToString.get should equal ("16")
  }

  test("""... literal string repetition ... ["Moe" x 5]""") {
    val result = interpretCode(""""Moe" x 5""")
    result.unboxToString.get should equal ("MoeMoeMoeMoeMoe")
  }

  test("""... literal string repetition ... ["Moe" x "5"]""") {
    val result = interpretCode(""""Moe" x "5"""")
    result.unboxToString.get should equal ("MoeMoeMoeMoeMoe")
  }

}
