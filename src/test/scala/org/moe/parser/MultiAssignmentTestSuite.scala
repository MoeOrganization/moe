package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class MultiAssignmentTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // declaration

  test("... basic multi-declaration") {
    val result = interpretCode("my ($x, $y) = (1, 2); [ $x, $y ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  test("... basic multi-declaration w/ extra var") {
    val result = interpretCode("my ($x, $y, $z) = (1, 2); [ $x, $y, $z ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
    a(2) should equal (runtime.NativeObjects.getUndef)
  }

  test("... basic multi-declaration w/ extra element") {
    val result = interpretCode("my ($x, $y) = (1, 2, 3); [ $x, $y ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  // assignment

  test("... basic multi-assign") {
    val result = interpretCode("my ($x, $y); ($x, $y) = (1, 2); [ $x, $y ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  test("... basic multi-assign w/ extra var") {
    val result = interpretCode("my ($x, $y, $z); ($x, $y, $z) = (1, 2); [ $x, $y, $z ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
    a(2) should equal (runtime.NativeObjects.getUndef)
  }

  test("... basic multi-assign w/ extra element") {
    val result = interpretCode("my ($x, $y); ($x, $y) = (1, 2, 3); [ $x, $y ]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  test("... basic multi-assign w/ swap") {
    val result = interpretCode("my ($x, $y) = (1, 2); ($x, $y) = ($y, $x); [$x, $y]")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (2)
    a(1).unboxToInt.get should equal (1)
  }

  // attribute assignment

  private val testClass = """
    class Foo {
      has $!x;
      has $!y; 
      has $!z; 

      method test1 {
        ($!x, $!y) = (1, 2);
      }

      method test2 {
        ($!x, $!y, $!z) = (1, 2);
      }

      method test3 {
        ($!x, $!y) = (1, 2, 3);
      }

      method test4 {
        ($!x, $!y) = (1, 2);
        ($!x, $!y) = ($!y, $!x);
      }

      method get { [ $!x, $!y, $!z ] }
    }
  """

  test("... basic multi-attribute-assign") {
    val result = interpretCode(testClass + "; my $x = Foo.new; $x.test1; $x.get")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  test("... basic multi-attribute-assign w/ extra var") {
    val result = interpretCode(testClass + "; my $x = Foo.new; $x.test2; $x.get")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
    a(2) should equal (runtime.NativeObjects.getUndef)
  }

  test("... basic multi-attribute-assign w/ extra element") {
    val result = interpretCode(testClass + "; my $x = Foo.new; $x.test3; $x.get")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (1)
    a(1).unboxToInt.get should equal (2)
  }

  test("... basic multi-attribute-assign w/ swap") {
    val result = interpretCode(testClass + "; my $x = Foo.new; $x.test4; $x.get")
    var a = result.unboxToArrayBuffer.get
    a(0).unboxToInt.get should equal (2)
    a(1).unboxToInt.get should equal (1)
  }

}
