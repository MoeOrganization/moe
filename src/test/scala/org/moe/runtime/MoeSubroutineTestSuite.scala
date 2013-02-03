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

}