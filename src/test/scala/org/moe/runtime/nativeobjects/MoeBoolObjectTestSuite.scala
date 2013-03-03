package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
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
    val o = new MoeBoolObject(true)
    assert(o.getNativeValue === true)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... false Boolean object") {
    val o = new MoeBoolObject(false)
    assert(o.getNativeValue === false)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Boolean object with class") {
    val c = new MoeClass("Boolean")
    val o = new MoeBoolObject(false, Some(c))
    assert(o.getAssociatedClass.get === c)
  }

}
