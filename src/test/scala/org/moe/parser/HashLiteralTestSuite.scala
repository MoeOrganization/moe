package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class HashLiteralTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... basic test with hashref") {
    val result = interpretCode("{foo => 'bar'}")
    val map = result.unboxToMap.get
    val key = map("foo")
    key.unboxToString.get should equal ("bar")
  }

  test("... basic test with hashref of a string key") {
    val result = interpretCode("{'foo' => 'bar'}")
    val map = result.unboxToMap.get
    val key = map("foo")
    key.unboxToString.get should equal ("bar")
  }

  test("... basic test with hash element assignment") {
    val result = interpretCode("my %h = {foo => 'bar'}; %h{'foo'} = 'baz'; %h")
    val map = result.unboxToMap.get
    val key = map("foo")
    key.unboxToString.get should equal ("baz")
  }

  test("... basic test with hash element assignment -- non-existent element") {
    val result = interpretCode("my %h = {foo => 'bar'}; %h{'baz'} = 'moe'; %h")
    val map = result.unboxToMap.get
    map("foo").unboxToString.get should equal ("bar")
    map("baz").unboxToString.get should equal ("moe")
  }

}
