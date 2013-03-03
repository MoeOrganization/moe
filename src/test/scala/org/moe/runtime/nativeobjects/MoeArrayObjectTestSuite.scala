package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeArrayObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Array object") {
    val o = r.NativeObjects.getArray(
      List(
        r.NativeObjects.getUndef,
        r.NativeObjects.getInt(10)
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
    val o = r.NativeObjects.getArray(List())
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Array").get)
  }

  test("... false Array object") {
    val o = r.NativeObjects.getArray(List())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Array object") {
    val o = r.NativeObjects.getArray(
      List(
        r.NativeObjects.getInt(10),
        r.NativeObjects.getArray(
          List(
            r.NativeObjects.getInt(42)
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
