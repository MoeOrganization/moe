package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeParameterTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  test("... basic parameter") {
    val param = new MoeParameter(name = "$x")
    assert(param.getName === "$x")
    assert(param.isOptional === false)
    assert(param.isSlurpy === false)
  }

  test("... basic optional parameter") {
    val param = new MoeParameter(name = "$x", optional = true)
    assert(param.getName === "$x")
    assert(param.isOptional === true)
    assert(param.isSlurpy === false)
  }

  test("... basic slupry parameter") {
    val param = new MoeParameter(name = "*@x", slurpy = true)
    assert(param.getName === "*@x")
    assert(param.isOptional === false)
    assert(param.isSlurpy === true)
  }
}