package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeParameterTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  test("... basic parameter") {
    val param = new MoePositionalParameter("$x")
    assert(param.getName === "$x")
  }

  test("... basic optional parameter") {
    val param = new MoeOptionalParameter("$x?")
    assert(param.getName === "$x?")
  }

  test("... basic slupry parameter") {
    val param = new MoeSlurpyParameter("*@x")
    assert(param.getName === "*@x")
  }
}