package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNumObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple Float object") {
    val o = new MoeNumObject(10.5)
    assert(o.getNativeValue === 10.5)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Float object with class") {
    val c = new MoeClass("Number")
    val o = new MoeNumObject(10.5, Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false Float object") {
    val o = new MoeNumObject(0.0)
    assert(o.getNativeValue === 0.0)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

}
