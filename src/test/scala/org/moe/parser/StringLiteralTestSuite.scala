package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class StringLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // double quotes ...

  test("... basic test with a simple double-quoted string") {
    val result = interpretCode(""" "hello world" """)
    result.unboxToString.get should equal ("hello world")
  }

  test("... basic test with a double-quoted string with control characters") {
    val result = interpretCode(""" "foo\tbar\n" """)
    result.unboxToString.get should equal ("foo\tbar\n")
  }

  test("... basic test with a double-quoted string with escaped quotes") {
    val result = interpretCode(""" "foo\"bar\"" """)
    result.unboxToString.get should equal ("foo\"bar\"")
  }

  test("... basic test with a double-quoted string with unicode escaped literals ") {
    val result = interpretCode(""" "\x{03a3}" """)
    result.unboxToString.get should equal ("\\x{03a3}")
  }

  test("... basic test with double-quoted string with leading whitespace") {
    val result = interpretCode(""" "    hello world" """)
    result.unboxToString.get should equal ("    hello world")
  }

  // single quotes ...

  test("... basic test with a simple single-quoted string") {
    val result = interpretCode("'hello world'")
    result.unboxToString.get should equal ("hello world")
  }

  test("... basic test with a single-quoted string with control characters") {
    val result = interpretCode("'foo\\tbar\\n'")
    result.unboxToString.get should equal ("foo\\tbar\\n")
  }

  test("... basic test with a single-quoted string with embedded double quotes") {
    val result = interpretCode("'foo\"bar\"'")
    result.unboxToString.get should equal ("foo\"bar\"")
  }

  test("... basic test with a single-quoted string with unicode escaped literals ") {
    val result = interpretCode("'\\x{03a3}'")
    result.unboxToString.get should equal ("\\x{03a3}")
  }

  test("... basic test with single-quoted string with leading whitespace") {
    val result = interpretCode(""" '    hello world' """)
    result.unboxToString.get should equal ("    hello world")
  }
}
