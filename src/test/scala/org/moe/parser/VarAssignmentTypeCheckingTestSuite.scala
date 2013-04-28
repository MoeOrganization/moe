package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class VarAssignmentTypeCheckingTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // ------------------------------------------------------
  // scalar (successful)
  // ------------------------------------------------------

  test("... simple scalar assignment w/ int") {
    val result = interpretCode("my $x = 10;")
    result.unboxToInt.get should equal (10)
  }

  test("... simple scalar assignment w/ float") {
    val result = interpretCode("my $x = 10.5;")
    result.unboxToDouble.get should equal (10.5)
  }

  test("... simple scalar assignment w/ string") {
    val result = interpretCode("my $x = 'foo';")
    result.unboxToString.get should equal ("foo")
  }

  test("... simple scalar assignment w/ boolean") {
    val result = interpretCode("my $x = true;")
    result.unboxToBoolean.get should equal (true)
  }

  test("... simple scalar assignment w/ undef") {
    val result = interpretCode("my $x = undef;")
    assert(result.isUndef === true)
  }

  // duplicate the tests with assignment seperate from declaration

  test("... simple scalar assignment #2 w/ int") {
    val result = interpretCode("my $x; $x = 10;")
    result.unboxToInt.get should equal (10)
  }

  test("... simple scalar assignment #2 w/ float") {
    val result = interpretCode("my $x; $x = 10.5;")
    result.unboxToDouble.get should equal (10.5)
  }

  test("... simple scalar assignment #2 w/ string") {
    val result = interpretCode("my $x; $x = 'foo';")
    result.unboxToString.get should equal ("foo")
  }

  test("... simple scalar assignment #2 w/ boolean") {
    val result = interpretCode("my $x; $x = true;")
    result.unboxToBoolean.get should equal (true)
  }

  // ------------------------------------------------------
  // scalar (failures)
  // ------------------------------------------------------

  test("... exceptional scalar assignment w/ array") {
    val e = intercept[Exception] {
      interpretCode("my $x = [];")
    }
    e.getMessage should equal ("the container ($x) is not compatible with ARRAY")
  }

  test("... exceptional scalar assignment w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my $x = {};")
    }
    e.getMessage should equal ("the container ($x) is not compatible with HASH")
  }

  test("... exceptional scalar assignment w/ code") {
    val e = intercept[Exception] {
      interpretCode("my $x = -> {};")
    }
    e.getMessage should equal ("the container ($x) is not compatible with CODE")
  }

  // duplicate the tests with assignment seperate from declaration

  test("... exceptional scalar assignment #2 w/ array") {
    val e = intercept[Exception] {
      interpretCode("my $x; $x = [];")
    }
    e.getMessage should equal ("the container ($x) is not compatible with ARRAY")
  }

  test("... exceptional scalar assignment #2 w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my $x; $x = {};")
    }
    e.getMessage should equal ("the container ($x) is not compatible with HASH")
  }

  test("... exceptional scalar assignment # w/ code") {
    val e = intercept[Exception] {
      interpretCode("my $x; $x = -> {};")
    }
    e.getMessage should equal ("the container ($x) is not compatible with CODE")
  }

  // ------------------------------------------------------
  // array (successful)
  // ------------------------------------------------------

  test("... simple array assignment w/ array") {
    val result = interpretCode("my @x = [ 1, 2, 3 ];")
    result.unboxToArrayBuffer.get.length should equal (3)
  }

  test("... simple array assignment w/ undef") {
    val result = interpretCode("my @x = undef;")
    assert(result.isUndef === true)
  }

  // duplicate the tests with assignment seperate from declaration

  test("... simple array assignment #2 w/ array") {
    val result = interpretCode("my @x; @x = [ 1, 2, 3 ];")
    result.unboxToArrayBuffer.get.length should equal (3)
  }

  // ------------------------------------------------------
  // array (failures)
  // ------------------------------------------------------

  test("... exceptional array assignment w/ int") {
    val e = intercept[Exception] {
      interpretCode("my @x = 1;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment w/ num") {
    val e = intercept[Exception] {
      interpretCode("my @x = 1.5;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment w/ str") {
    val e = intercept[Exception] {
      interpretCode("my @x = 'foo';")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my @x = true;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my @x = {};")
    }
    e.getMessage should equal ("the container (@x) is not compatible with HASH")
  }

  test("... exceptional array assignment w/ code") {
    val e = intercept[Exception] {
      interpretCode("my @x = -> {};")
    }
    e.getMessage should equal ("the container (@x) is not compatible with CODE")
  }

  // duplicate the tests with assignment seperate from declaration

  test("... exceptional array assignment #2 w/ int") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = 1;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment #2 w/ num") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = 1.5;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment #2 w/ str") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = 'foo';")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment #2 w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = true;")
    }
    e.getMessage should equal ("the container (@x) is not compatible with SCALAR")
  }

  test("... exceptional array assignment #2 w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = {};")
    }
    e.getMessage should equal ("the container (@x) is not compatible with HASH")
  }

  test("... exceptional array assignment #2 w/ code") {
    val e = intercept[Exception] {
      interpretCode("my @x; @x = -> {};")
    }
    e.getMessage should equal ("the container (@x) is not compatible with CODE")
  }

  // ------------------------------------------------------
  // hash (successful)
  // ------------------------------------------------------

  test("... simple hash assignment w/ hash") {
    val result = interpretCode("my %x = { one => 1 };")
    result.unboxToMap.get.get("one").get.unboxToInt.get should equal (1)
  }

  test("... simple hash assignment w/ undef") {
    val result = interpretCode("my %x = undef;")
    assert(result.isUndef === true)
  }

  // duplicate the tests with assignment seperate from declaration

  test("... simple hash assignment #2 w/ hash") {
    val result = interpretCode("my %x; %x = { one => 1 };")
    result.unboxToMap.get.get("one").get.unboxToInt.get should equal (1)
  }

  // ------------------------------------------------------
  // hash (failures)
  // ------------------------------------------------------

  test("... exceptional hash assignment w/ int") {
    val e = intercept[Exception] {
      interpretCode("my %x = 1;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment w/ num") {
    val e = intercept[Exception] {
      interpretCode("my %x = 1.5;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment w/ str") {
    val e = intercept[Exception] {
      interpretCode("my %x = 'foo';")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my %x = true;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment w/ array") {
    val e = intercept[Exception] {
      interpretCode("my %x = [];")
    }
    e.getMessage should equal ("the container (%x) is not compatible with ARRAY")
  }

  test("... exceptional hash assignment w/ code") {
    val e = intercept[Exception] {
      interpretCode("my %x = -> {};")
    }
    e.getMessage should equal ("the container (%x) is not compatible with CODE")
  }

  // duplicate the tests with assignment seperate from declaration

  test("... exceptional hash assignment #2 w/ int") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = 1;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment #2 w/ num") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = 1.5;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment #2 w/ str") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = 'foo';")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment #2 w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = true;")
    }
    e.getMessage should equal ("the container (%x) is not compatible with SCALAR")
  }

  test("... exceptional hash assignment #2 w/ array") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = [];")
    }
    e.getMessage should equal ("the container (%x) is not compatible with ARRAY")
  }

  test("... exceptional hash assignment #2 w/ code") {
    val e = intercept[Exception] {
      interpretCode("my %x; %x = -> {};")
    }
    e.getMessage should equal ("the container (%x) is not compatible with CODE")
  }

  // ------------------------------------------------------
  // code (successful)
  // ------------------------------------------------------

  test("... simple code assignment w/ code") {
    val result = interpretCode("my &x = -> { 1 };")
    assert(result.isInstanceOf[MoeCode] === true)
  }

  test("... simple code assignment w/ undef") {
    val result = interpretCode("my &x = undef;")
    assert(result.isUndef === true)
  }

  // duplicate the tests with assignment seperate from declaration

  test("... simple code assignment #2 w/ code") {
    val result = interpretCode("my &x; &x = -> { 1 };")
    assert(result.isInstanceOf[MoeCode] === true)
  }

  // ------------------------------------------------------
  // code (failures)
  // ------------------------------------------------------

  test("... exceptional code assignment w/ int") {
    val e = intercept[Exception] {
      interpretCode("my &x = 1;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment w/ num") {
    val e = intercept[Exception] {
      interpretCode("my &x = 1.5;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment w/ str") {
    val e = intercept[Exception] {
      interpretCode("my &x = 'foo';")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my &x = true;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment w/ array") {
    val e = intercept[Exception] {
      interpretCode("my &x = [];")
    }
    e.getMessage should equal ("the container (&x) is not compatible with ARRAY")
  }

  test("... exceptional code assignment w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my &x = {};")
    }
    e.getMessage should equal ("the container (&x) is not compatible with HASH")
  }

  // duplicate the tests with assignment seperate from declaration

  test("... exceptional code assignment #2 w/ int") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = 1;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment #2 w/ num") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = 1.5;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment #2 w/ str") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = 'foo';")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment #2 w/ boolean") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = true;")
    }
    e.getMessage should equal ("the container (&x) is not compatible with SCALAR")
  }

  test("... exceptional code assignment #2 w/ array") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = [];")
    }
    e.getMessage should equal ("the container (&x) is not compatible with ARRAY")
  }

  test("... exceptional code assignment #2 w/ hash") {
    val e = intercept[Exception] {
      interpretCode("my &x; &x = {};")
    }
    e.getMessage should equal ("the container (&x) is not compatible with HASH")
  }


}
