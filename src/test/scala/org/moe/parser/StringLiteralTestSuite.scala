package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class StringLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with a simple double-quoted string") {
    val result = interpretCode("\"hello world\"")
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

  test("... basic test with a simple single-quoted string") {
    val result = interpretCode("'hello world'")
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "hello world")
  }

}
