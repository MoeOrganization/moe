package org.moe.runtime.nativeobjects

import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeBoolObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Boolean object") {
    val o = r.NativeObjects.getTrue
    assert(o.getNativeValue === true)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... false Boolean object") {
    val o = r.NativeObjects.getFalse
    assert(o.getNativeValue === false)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Boolean object with class") {
    val o = r.NativeObjects.getTrue
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Bool").get)
  }

}
