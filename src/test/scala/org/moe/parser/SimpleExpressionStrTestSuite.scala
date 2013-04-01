package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionStrTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... literal string concatenation ... [\"Hello \" . \"Moe\"]") {
    val result = interpretCode("\"Hello \" ~ \"Moe\"")
    result.unboxToString.get should equal ("Hello Moe")
  }

  test("... literal string concatenation ... ['foo' . 'bar']") {
    val result = interpretCode("'foo' ~ 'bar'")
    result.unboxToString.get should equal ("foobar")
  }

/*
  test("... literal string concatenation ... ['1' . 2]") {
    val result = interpretCode("'1' . 2")
    result.unboxToString.get should equal ("12")
  }

  test("... literal string concatenation ... ['1' . 2 * 3]") {
    val result = interpretCode("'1' . 2 * 3")
    result.unboxToString.get should equal ("16")
  }
*/

  test("""... literal string repetition ... ["Moe" x 5]""") {
    val result = interpretCode(""""Moe" x 5""")
    result.unboxToString.get should equal ("MoeMoeMoeMoeMoe")
  }

/*
  test("""... literal string repetition ... ["Moe" x "5"]""") {
    val result = interpretCode(""""Moe" x "5"""")
    result.unboxToString.get should equal ("MoeMoeMoeMoeMoe")
  }
*/

  // equality operators

  test("""... literal string equality operation ... ["Moe" eq "Moe"]""") {
    val result = interpretCode(""""Moe" eq "Moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string equality operation ... ["Moe" eq "moe"]""") {
    val result = interpretCode(""""Moe" eq "moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string equality operation ... ["Moe" ne "Moe"]""") {
    val result = interpretCode(""""Moe" ne "Moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string equality operation ... ["Moe" ne "moe"]""") {
    val result = interpretCode(""""Moe" ne "moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string equality operation ... ["Moe" cmp "moe"]""") {
    val result = interpretCode(""""Moe" cmp "moe"""")
    result.unboxToInt.get should equal (-1)
  }

  test("""... literal string equality operation ... ["Moe" cmp "Moe"]""") {
    val result = interpretCode(""""Moe" cmp "Moe"""")
    result.unboxToInt.get should equal (0)
  }

  test("""... literal string equality operation ... ["moe" cmp "Moe"]""") {
    val result = interpretCode(""""moe" cmp "Moe"""")
    result.unboxToInt.get should equal (1)
  }

  // relational operators

  test("""... literal string relational operation lt ... ["Moe" lt "moe"]""") {
    val result = interpretCode(""""Moe" lt "moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string relational operation lt ... ["moe" lt "Moe"]""") {
    val result = interpretCode(""""moe" lt "Moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation lt ... ["Moe" lt "Moe"]""") {
    val result = interpretCode(""""Moe" lt "Moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation gt ... ["Moe" gt "moe"]""") {
    val result = interpretCode(""""Moe" gt "moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation gt ... ["moe" gt "Moe"]""") {
    val result = interpretCode(""""moe" gt "Moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string relational operation gt ... ["Moe" gt "Moe"]""") {
    val result = interpretCode(""""Moe" gt "Moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation le ... ["Moe" le "Moe"]""") {
    val result = interpretCode(""""Moe" le "Moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string relational operation le ... ["Moe" le "moe"]""") {
    val result = interpretCode(""""Moe" le "moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string relational operation le ... ["moe" le "Moe"]""") {
    val result = interpretCode(""""moe" le "Moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation ge ... ["Moe" ge "Moe"]""") {
    val result = interpretCode(""""Moe" ge "Moe"""")
    result.unboxToBoolean.get should equal (true)
  }

  test("""... literal string relational operation ge ... ["Moe" ge "moe"]""") {
    val result = interpretCode(""""Moe" ge "moe"""")
    result.unboxToBoolean.get should equal (false)
  }

  test("""... literal string relational operation ge ... ["moe" ge "Moe"]""") {
    val result = interpretCode(""""moe" ge "Moe"""")
    result.unboxToBoolean.get should equal (true)
  }

}
