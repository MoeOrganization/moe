package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeMethodTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... basic method") {
    val env      = r.getRootEnv
    val invocant = new MoeObject()
    val method   = new MoeMethod("ident", new MoeSignature(), env, (e) => e.getCurrentInvocant.get)
    val result   = method.execute(new MoeArguments(List(), Some(invocant)))
    assert(result === invocant)
  }

}