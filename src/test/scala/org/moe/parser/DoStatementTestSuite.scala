package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class DoStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  // TODO test environment stack via syntax when we are far enough
  test("... a do block") {
    val result = interpretCode("do { 100 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 100)
  }

  test("... a multi-statement do block") {
    val result = interpretCode("do { 100; 200 }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... a do block with semicolons at the end") {
    val result = interpretCode("do { 100; 200; }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }

  test("... a do block with multiple semicolons at the end") {
    val result = interpretCode("do { 100; 200;; }")
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 200)
  }


}
