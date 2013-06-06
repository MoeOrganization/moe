package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class MultiVarAssignmentTypeCheckingTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  // ------------------------------------------------------
  // multi-assignment
  // ------------------------------------------------------

  test("... multi-assignment success") {
    val result = interpretCode("my ($i, $n, $s, @a, %h, &c) = (10, 10.5, 'foo', [], {}, () => {});")
    assert(result.isInstanceOf[MoeCode] === true)
  }

  test("... multi-assignment success #2") {
    val result = interpretCode("my ($i, $n, $s, @a, %h, &c); ($i, $n, $s, @a, %h, &c) = (10, 10.5, 'foo', [], {}, () => {});")
    assert(result.isInstanceOf[MoeCode] === true)
  }

  test("... multi-assignment success #3") {
    val result = interpretCode("my ($i, $n, $s, @a, %h, &c) = ([], 10.5, 'foo', [], {}, () => {}); $i")
    assert(result.isInstanceOf[MoeArrayObject] === true)
  }

  // failures

  test("... multi-assignment failure #1") {
    val e = intercept[Exception] {
      interpretCode("my ($i, $n, $s, @a, %h, &c) = (10, 10.5, 'foo', 10, {}, () => {});")
    }
    e.getMessage should equal ("the container (@a) is not compatible with SCALAR")
  }

  test("... multi-assignment failure #2") {
    val e = intercept[Exception] {
      interpretCode("my ($i, $n, $s, @a, %h, &c) = (10, 10.5, 'foo', [], 'bar', () => {});")
    }
    e.getMessage should equal ("the container (%h) is not compatible with SCALAR")
  }

}
