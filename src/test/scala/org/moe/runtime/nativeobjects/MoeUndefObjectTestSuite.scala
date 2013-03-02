package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeUndefObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple Undef object") {
    val o = new MoeUndefObject()
    assert(o.getNativeValue == null)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(o.isUndef)
    assert(o.toString === "undef")
  }

  test("... simple Undef object with class") {
    val c = new MoeClass("Undef")
    val o = new MoeUndefObject(Some(c))
    assert(o.getAssociatedClass.get === c)
  }

}
