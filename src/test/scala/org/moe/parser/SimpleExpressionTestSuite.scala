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
    result.asInstanceOf[MoeIntObject].getNativeValue should equal (4)
  }

}
