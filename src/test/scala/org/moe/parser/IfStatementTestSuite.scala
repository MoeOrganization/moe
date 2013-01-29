package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class IfStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a weird looking if") {
    val result = interpretCode("if(true) { 2; 7 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 7)
  }

}
