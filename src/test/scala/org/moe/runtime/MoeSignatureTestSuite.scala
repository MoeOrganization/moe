package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeSignatureTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  test("... basic signature") {
    val param = new MoeParameter(name = "$x")
    val sig = new MoeSignature(List(param))

    val params = sig.getParams
    assert(params(0) === param)
  }

}