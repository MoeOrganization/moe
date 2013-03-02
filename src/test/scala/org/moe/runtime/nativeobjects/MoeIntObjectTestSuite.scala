package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeIntObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple Int object") {
    val o = new MoeIntObject(10)
    assert(o.getNativeValue === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Int object with class") {
    val c = new MoeClass("Number")
    val o = new MoeIntObject(10, Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false Int object") {
    val o = new MoeIntObject(0)
    assert(o.getNativeValue === 0)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

}
