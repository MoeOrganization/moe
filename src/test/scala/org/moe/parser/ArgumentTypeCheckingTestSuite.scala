package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArgumentTypeCheckingTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // ------------------------------------------------------
  // scalar tests
  // ------------------------------------------------------

  private val scalarExamples = Map(
    "positional" -> "sub foo ($x) { $x }; foo(%s)", 
    "optional"   -> "sub foo ($x?) { $x }; foo(%s)",
    "named"      -> "sub foo (:$x) { $x }; foo(x => %s)"
  )

  for ((name, code) <- scalarExamples) {

    // ------------------------------------------------------
    // scalar (successful)
    // ------------------------------------------------------

    test("... simple scalar assignment w/ int [" + name + "]") {
      val result = interpretCode(code.format("10"))
      result.unboxToInt.get should equal (10)
    }

    test("... simple scalar assignment w/ float [" + name + "]") {
      val result = interpretCode(code.format("10.5"))
      result.unboxToDouble.get should equal (10.5)
    }

    test("... simple scalar assignment w/ string [" + name + "]") {
      val result = interpretCode(code.format("'foo'"))
      result.unboxToString.get should equal ("foo")
    }

    test("... simple scalar assignment w/ boolean [" + name + "]") {
      val result = interpretCode(code.format("true"))
      result.unboxToBoolean.get should equal (true)
    }

    test("... simple scalar assignment w/ undef [" + name + "]") {
      val result = interpretCode(code.format("undef"))
      assert(result.isUndef === true)
    }

    // ------------------------------------------------------
    // scalar (failures)
    // ------------------------------------------------------

    test("... exceptional scalar assignment w/ array [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("[]"))
      }
      e.getMessage should equal ("the argument ($x) is not compatible with ARRAY")
    }

    test("... exceptional scalar assignment w/ hash [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("{}"))
      }
      e.getMessage should equal ("the argument ($x) is not compatible with HASH")
    }

    test("... exceptional scalar assignment w/ code [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("->{}"))
      }
      e.getMessage should equal ("the argument ($x) is not compatible with CODE")
    }
  }

  // ------------------------------------------------------
  // array tests
  // ------------------------------------------------------

  private val arrayExamples = Map(
    "positional" -> "sub foo (@x) { @x }; foo(%s)", 
    "optional"   -> "sub foo (@x?) { @x }; foo(%s)",
    "named"      -> "sub foo (:@x) { @x }; foo(x => %s)"
  )

  for ((name, code) <- arrayExamples) {

    // ------------------------------------------------------
    // array (successful)
    // ------------------------------------------------------

    test("... simple array assignment w/ array [" + name + "]") {
      val result = interpretCode(code.format("[ 1, 2, 3 ]"))
      result.unboxToArrayBuffer.get.length should equal (3)
    }

    test("... simple array assignment w/ undef [" + name + "]") {
      val result = interpretCode(code.format("undef"))
      assert(result.isUndef === true)
    }

    // ------------------------------------------------------
    // array (failures)
    // ------------------------------------------------------

    test("... exceptional array assignment w/ int [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with SCALAR")
    }

    test("... exceptional array assignment w/ num [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1.5"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with SCALAR")
    }

    test("... exceptional array assignment w/ str [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("'foo'"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with SCALAR")
    }

    test("... exceptional array assignment w/ boolean [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("true"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with SCALAR")
    }

    test("... exceptional array assignment w/ hash [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("{}"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with HASH")
    }

    test("... exceptional array assignment w/ code [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("-> {}"))
      }
      e.getMessage should equal ("the argument (@x) is not compatible with CODE")
    }
  }

  // ------------------------------------------------------
  // hash tests
  // ------------------------------------------------------

  private val hashExamples = Map(
    "positional" -> "sub foo (%%x) { %%x }; foo(%s)", 
    "optional"   -> "sub foo (%%x?) { %%x }; foo(%s)",
    "named"      -> "sub foo (:%%x) { %%x }; foo(x => %s)"
  )

  for ((name, code) <- hashExamples) {  

    // ------------------------------------------------------
    // hash (successful)
    // ------------------------------------------------------

    test("... simple hash assignment w/ hash [" + name + "]") {
      val result = interpretCode(code.format("{ one => 1 }"))
      result.unboxToMap.get.get("one").get.unboxToInt.get should equal (1)
    }

    test("... simple hash assignment w/ undef [" + name + "]") {
      val result = interpretCode(code.format("undef"))
      assert(result.isUndef === true)
    }

    // ------------------------------------------------------
    // hash (failures)
    // ------------------------------------------------------

    test("... exceptional hash assignment w/ int [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with SCALAR")
    }

    test("... exceptional hash assignment w/ num [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1.5"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with SCALAR")
    }

    test("... exceptional hash assignment w/ str [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("'foo'"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with SCALAR")
    }

    test("... exceptional hash assignment w/ boolean [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("true"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with SCALAR")
    }

    test("... exceptional hash assignment w/ array [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("[]"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with ARRAY")
    }

    test("... exceptional hash assignment w/ code [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("-> {}"))
      }
      e.getMessage should equal ("the argument (%x) is not compatible with CODE")
    }
  }

  // ------------------------------------------------------
  // code tests
  // ------------------------------------------------------

  private val codeExamples = Map(
    "positional" -> "sub foo (&x) { &x }; foo(%s)", 
    "optional"   -> "sub foo (&x?) { &x }; foo(%s)",
    "named"      -> "sub foo (:&x) { &x }; foo(x => %s)"
  )

  for ((name, code) <- codeExamples) {    

    // ------------------------------------------------------
    // code (successful)
    // ------------------------------------------------------

    test("... simple code assignment w/ code [" + name + "]") {
      val result = interpretCode(code.format("-> { 1 }"))
      assert(result.isInstanceOf[MoeCode] === true)
    }

    test("... simple code assignment w/ undef [" + name + "]") {
      val result = interpretCode(code.format("undef"))
      assert(result.isUndef === true)
    }

    // ------------------------------------------------------
    // code (failures)
    // ------------------------------------------------------

    test("... exceptional code assignment w/ int [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with SCALAR")
    }

    test("... exceptional code assignment w/ num [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("1.5"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with SCALAR")
    }

    test("... exceptional code assignment w/ str [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("'foo'"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with SCALAR")
    }

    test("... exceptional code assignment w/ boolean [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("true"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with SCALAR")
    }

    test("... exceptional code assignment w/ array [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("[]"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with ARRAY")
    }

    test("... exceptional code assignment w/ hash [" + name + "]") {
      val e = intercept[Exception] {
        interpretCode(code.format("{}"))
      }
      e.getMessage should equal ("the argument (&x) is not compatible with HASH")
    }
  }

}
