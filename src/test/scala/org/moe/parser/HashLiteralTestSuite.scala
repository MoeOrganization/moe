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

  test("... basic test for nested hash element access") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => 5}}; %h{'three'}{'four'}""")
    result.unboxToInt.get should equal (4);
  }

  test("... basic test for nested hash element access -- non-existent element") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => 5}}; %h{'three'}{'six'}""")
    assert(result.isUndef);
  }

  test("... basic test for nested hash element access -- three levels") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => {six => 6, seven => 7}}}; %h{'three'}{'five'}{'seven'}""")
    result.unboxToInt.get should equal (7);
  }

  test("... basic test for nested hash element assignmemt") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => 5}}; %h{'three'}{'four'} = 42; %h{'three'}{'four'}""")
    result.unboxToInt.get should equal (42);
  }

  test("... basic test for nested hash element assignment -- non-existent element") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => 5}}; %h{'three'}{'six'} = 6; %h{'three'}{'six'}""")
    result.unboxToInt.get should equal (6);
  }

  test("... basic test for nested hash element assignment -- three levels") {
    val result = interpretCode("""my %h = {one => 1, two => 2, three => {four => 4, five => {six => 6, seven => 7}}}; %h{'three'}{'five'}{'seven'} = 42; %h{'three'}{'five'}{'seven'}""")
    result.unboxToInt.get should equal (42);
  }

}
