package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class StringLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  // double quotes ...

  test("... basic test with a simple double-quoted string") {
    val result = interpretCode(""" "hello world" """)
    assert(result.unboxToString.get === "hello world")
  }

  test("... basic test with a double-quoted string with control characters") {
    val result = interpretCode(""" "foo\tbar\n" """)
    assert(result.unboxToString.get === "foo\\tbar\\n")
  }

  test("... basic test with a double-quoted string with escaped quotes") {
    val result = interpretCode(""" "foo\"bar\"" """)
    assert(result.unboxToString.get === "foo\\\"bar\\\"")
  }

  //
  test("... basic test with a double-quoted string with unicode escaped literals ") {
    val result = interpretCode(""" "\x{03a3}" """)
    assert(result.unboxToString.get === "\\x{03a3}")
  }

  // single quotes ...

  test("... basic test with a simple single-quoted string") {
    val result = interpretCode("'hello world'")
    assert(result.unboxToString.get === "hello world")
  }

  test("... basic test with a single-quoted string with control characters") {
    val result = interpretCode("'foo\\tbar\\n'")
    assert(result.unboxToString.get === "foo\\tbar\\n")
  }

  test("... basic test with a single-quoted string with embedded double quotes") {
    val result = interpretCode("'foo\"bar\"'")
    assert(result.unboxToString.get === "foo\"bar\"")
  }

  test("... basic test with a single-quoted string with unicode escaped literals ") {
    val result = interpretCode("'\\x{03a3}'")
    assert(result.unboxToString.get === "\\x{03a3}")
  }

}
