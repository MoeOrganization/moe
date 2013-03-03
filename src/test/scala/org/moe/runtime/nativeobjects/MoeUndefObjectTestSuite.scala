package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeUndefObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Undef object") {
    val o = r.NativeObjects.getUndef
    assert(o.getNativeValue == null)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(o.isUndef)
    assert(o.toString === "undef")
  }

  test("... simple Undef object with class") {
    val o = r.NativeObjects.getUndef
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Undef").get)
  }

}
