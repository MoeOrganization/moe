package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class IOClassTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // NOTE: These tests depend on the file t/000-load.t to exist.
  //   Should the contents of this file change, the tests must also be
  //   updated appropriately.
  
  private val goodPath = """my $path = "t/000-load.t";"""
  private val badPath  = """my $path = "t/000-load.x";"""

  test("... basic IO test -- .new") {
    val result = interpretCode(goodPath + """my $io = ^IO.new($path); $io.isa("IO")""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic IO test -- existence") {
    val result = interpretCode(goodPath + """my $io = ^IO.new($path); -e $io""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic IO test -- existence with invalid file") {
    val result = interpretCode(badPath + """my $io = ^IO.new($path); -e $io""")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic IO test -- readable") {
    val result = interpretCode(goodPath + """my $io = ^IO.new($path); -r $io""")
    result.unboxToBoolean.get should equal (true)
  }

  test("... basic IO test -- readable with invalid file") {
    val result = interpretCode(badPath + """my $io = ^IO.new($path); -r $io""")
    result.unboxToBoolean.get should equal (false)
  }

  test("... basic IO test -- readline") {
    val result = interpretCode(goodPath + """my $io = ^IO.new($path); $io.readline""")
    result.unboxToString.get should equal ("use Test::More;")
  }

  test("... basic IO test -- readlines") {
    val result = interpretCode(goodPath + """my $io = ^IO.new($path); $io.readlines""")
    val lines = result.unboxToArrayBuffer.get
    lines(0).unboxToString.get should equal ("use Test::More;")
    lines(1).unboxToString.get should equal ("")
    lines(2).unboxToString.get should equal ("plan(1);")
    lines(3).unboxToString.get should equal ("")
    lines(4).unboxToString.get should equal ("""ok(true, "... this worked!");""")
  }
}
