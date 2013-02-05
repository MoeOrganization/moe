package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class HashLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... basic test with hashref") {
    val result = interpretCode("{foo => 'bar'}")
    val map = result.asInstanceOf[MoeHashObject].getNativeValue
    val key = map("foo")
    assert(key.asInstanceOf[MoeStringObject].getNativeValue === "bar")
  }

  test("... basic test with hashref of a string key") {
    val result = interpretCode("{'foo' => 'bar'}")
    val map = result.asInstanceOf[MoeHashObject].getNativeValue
    val key = map("foo")
    assert(key.asInstanceOf[MoeStringObject].getNativeValue === "bar")
  }


}
