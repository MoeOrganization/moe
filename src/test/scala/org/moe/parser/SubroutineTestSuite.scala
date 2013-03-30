package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SubroutineTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... a basic subroutine") {
    val result = interpretCode("sub foo { 10 }; foo()")
    result.unboxToInt.get should equal (10)
  }

  test("... a basic subroutine w/ simple param") {
    val result = interpretCode("sub foo ($x) { $x * 10 }; foo(5)")
    result.unboxToInt.get should equal (50)
  }

  test("... a basic subroutine w/ simple params") {
    val result = interpretCode("sub foo ($x, $y) { $x * $y }; foo(5, 10)")
    result.unboxToInt.get should equal (50)
  }

  test("... a basic subroutine w/ optional params") {
    val result = interpretCode("sub foo ($x, $y?) { $x + ($y || 0) }; foo(5)")
    result.unboxToInt.get should equal (5)
  }

  test("... a basic subroutine w/ optional params (take 2)") {
    val result = interpretCode("sub foo ($x?, $y?) { ($x || 0) + ($y || 0) }; foo(5)")
    result.unboxToInt.get should equal (5)
  }

  test("... a basic subroutine w/ optional params (take 3)") {
    val result = interpretCode("sub foo ($x?, $y?) { ($x || 0) + ($y || 0) }; foo(5, 5)")
    result.unboxToInt.get should equal (10)
  }

  test("... a basic subroutine w/ optional params (take 4)") {
    val result = interpretCode("sub foo ($x?, $y?) { ($x || 0) + ($y || 0) }; foo()")
    result.unboxToInt.get should equal (0)
  }

  test("... a basic subroutine w/ optional params (satisified)") {
    val result = interpretCode("sub foo ($x, $y?) { $x + ($y || 0) }; foo(5, 5)")
    result.unboxToInt.get should equal (10)
  }

  test("... a basic subroutine w/ slurpy params") {
    val result = interpretCode("sub foo (*@_) { @_[0] + @_[1] }; foo(5, 5)")
    result.unboxToInt.get should equal (10)
  }

  test("... a basic subroutine w/ slurpy params (take 2)") {
    val result = interpretCode("sub foo (*@_) { @_->length }; foo(1, 2, 3)")
    result.unboxToInt.get should equal (3)
  }

  test("... a basic subroutine w/ named params") {
    val result = interpretCode("sub foo (:$x, :$y) { [ $x, $y ] }; foo( x => 10, y => 20 )")
    result.toString should equal ("[10, 20]")
  }

  test("... a basic subroutine w/ named params (out of order)") {
    val result = interpretCode("sub foo (:$x, :$y) { [ $x, $y ] }; foo( y => 20, x => 10 )")
    result.toString should equal ("[10, 20]")
  }

  test("... a basic subroutine w/ named params and positional") {
    val result = interpretCode("sub foo ($a, :$x, :$y) { [ $a, $x, $y ] }; foo( 5, x => 10, y => 20 )")
    result.toString should equal ("[5, 10, 20]")
  }

  test("... a basic subroutine w/ named params and positional (out of order)") {
    val result = interpretCode("sub foo ($a, :$x, :$y) { [ $a, $x, $y ] }; foo( 5, y => 20, x => 10 )")
    result.toString should equal ("[5, 10, 20]")
  }

  private val sum = """
    sub sum (@x, $acc?) {  
      @x->length == 0 
        ? ($acc || 0)
        : sum( @x->tail, @x->head + ($acc || 0) ) 
    }
  """

  test("... a recursive subroutine (take 1)") {
    val result = interpretCode(s"$sum; sum([])")
    result.unboxToInt.get should equal (0)
  }

  test("... a recursive subroutine (take 2)") {
    val result = interpretCode(s"$sum; sum([1, 1, 1])")
    result.unboxToInt.get should equal (3)
  }

  test("... a recursive subroutine (take 3)") {
    val result = interpretCode(s"$sum; sum(1 .. 10)")
    result.unboxToInt.get should equal (55)
  }

}
