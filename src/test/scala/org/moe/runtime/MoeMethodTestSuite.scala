package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeMethodTestSuite extends FunSuite with BeforeAndAfter {

  test("... basic method") {
    val env      = new MoeEnvironment()
    val invocant = new MoeObject()
    val method   = new MoeMethod("ident", new MoeSignature(), env, (e) => e.getCurrentInvocant.get)
    val result   = method.execute(new MoeArguments(List(), Some(invocant)))
    assert(result === invocant)
  }

}