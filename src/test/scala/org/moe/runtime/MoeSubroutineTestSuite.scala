package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeSubroutineTestSuite extends FunSuite with BeforeAndAfter {

  test("... basic sub") {
    var value  = new MoeObject()
    val sub    = new MoeSubroutine("ident", (args) => args(0))
    val result = sub.execute(List(value))
    assert(result === value)
  }

  test("... basic yadda-yadda-yadda sub") {
    val sub = new MoeSubroutine("yadda_yadda_yadda")
    intercept[MoeRuntime.Errors.UndefinedSubroutine] {
        sub.execute(List())
    }
  }

}