package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeArrayObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple Array object") {
    val o = new MoeArrayObject(
      List(
        new MoeUndefObject(),
        new MoeIntObject(10)
      )
    )
    val array = o.getNativeValue
    assert(array(0).isUndef)
    assert(array(1).unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Array object with class") {
    val c = new MoeClass("Array")
    val o = new MoeArrayObject(List(), Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false Array object") {
    val o = new MoeArrayObject(List())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Array object") {
    val o = new MoeArrayObject(
      List(
        new MoeIntObject(10),
        new MoeArrayObject(
          List(
            new MoeIntObject(42)
          )
        )
      )
     )
    val array = o.getNativeValue
    assert(array(0).unboxToInt.get === 10)
    val nested = array(1).unboxToList.get
    assert(nested(0).unboxToInt.get === 42)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

}
