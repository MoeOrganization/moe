package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._
import org.moe.runtime.nativeobjects._

class RegexTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... basic test with regex match -- string pattern") {
    val result = interpretCode(""""foo".match("foo")""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with regex match -- delimited pattern") {
    val result = interpretCode(""""foo".match(/foo/)""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with regex match -- anchored at beginning") {
    val result = interpretCode(""""foo".match(/^f/)""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with regex match -- anchored at end") {
    val result = interpretCode(""""foo".match(/o$/)""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with regex match -- string pattern -- no match") {
    val result = interpretCode(""""foo".match("f00")""")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic test with regex match -- variable with delimited pattern") {
    val result = interpretCode("""my $foo = "foo"; $foo.match(/foo/)""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic test with regex match -- delimited pattern -- no match") {
    val result = interpretCode(""""foo".match("f00")""")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic test with regex substitution -- string pattern") {
    val result = interpretCode(""""foo".subst("f", "F")""")
    result.unboxToString.get should equal ("Foo")
  }

  test("... basic test with regex substitution -- delimited pattern") {
    val result = interpretCode(""""foo".subst(/o+$/, "00")""")
    result.unboxToString.get should equal ("f00")
  }

  test("... basic test with regex substitution -- string pattern -- no match") {
    val result = interpretCode(""""foo".subst("a", "A")""")
    result.unboxToString.get should equal ("foo")
  }

  test("... basic test with regex substitution -- delimited pattern -- no match") {
    val result = interpretCode(""""foo".subst(/O+$/, "00")""")
    result.unboxToString.get should equal ("foo")
  }
}
