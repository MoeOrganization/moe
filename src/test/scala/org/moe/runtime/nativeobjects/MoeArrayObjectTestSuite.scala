package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
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
      ArrayBuffer(
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
    val o = r.NativeObjects.getArray(ArrayBuffer())
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Array").get)
  }

  test("... false Array object") {
    val o = r.NativeObjects.getArray(ArrayBuffer())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Array object") {
    val o = r.NativeObjects.getArray(
      ArrayBuffer(
        r.NativeObjects.getInt(10),
        r.NativeObjects.getArray(
          ArrayBuffer(
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

  test("... Array object length (empty)") {
    val o = r.NativeObjects.getArray( ArrayBuffer())
    assert(0 == o.length(r).unboxToInt.get, "empty array length == 0")
  }

  test("... Array object length") {
    val o = r.NativeObjects.getArray(
      ArrayBuffer(
        r.NativeObjects.getInt(1),
        r.NativeObjects.getInt(2),
        r.NativeObjects.getInt(3)
      )
     )
    val array = o.getNativeValue
    assert(array.length == o.length(r).unboxToInt.get, "Expected " + o.length(r) + " to equal " + array.length)
  }

  test("... Array object clear") {
    val o = r.NativeObjects.getArray(
      ArrayBuffer(
        r.NativeObjects.getInt(1),
        r.NativeObjects.getInt(2),
        r.NativeObjects.getInt(3)
      )
     )
    o.clear(r)
    assert(0 == o.length(r).unboxToInt.get)
  }

  test("... Array object shift") {
    val o = r.NativeObjects.getArray(
      ArrayBuffer(
        r.NativeObjects.getInt(1),
        r.NativeObjects.getInt(2),
        r.NativeObjects.getInt(3)
      )
     )
    assert(1 == o.shift(r).unboxToInt.get, "shift first item")
    assert(2 == o.shift(r).unboxToInt.get, "shift second item")
    assert(3 == o.shift(r).unboxToInt.get, "shift third item")
    assert(0 == o.length(r).unboxToInt.get, "Array is empty (" + o.length(r) + ")")
    assert(o.shift(r).isUndef, "shift on empty array")
  }

  test("... Array object pop") {
    val o = r.NativeObjects.getArray(
      ArrayBuffer(
        r.NativeObjects.getInt(1),
        r.NativeObjects.getInt(2),
        r.NativeObjects.getInt(3)
      )
     )
    assert(3 == o.pop(r).unboxToInt.get, "pop third item")
    assert(2 == o.pop(r).unboxToInt.get, "pop second item")
    assert(1 == o.pop(r).unboxToInt.get, "pop first item")
    assert(0 == o.length(r).unboxToInt.get, "Array is empty (" + o.length(r) + ")")
    assert(o.pop(r).isUndef, "pop on empty array")
  }

  test("... Array object unshift") {
    val o = r.NativeObjects.getArray( ArrayBuffer())
    assert(0 == o.unshift(r).unboxToInt.get, "unshift no items onto empty array")
    assert(0 == o.length(r).unboxToInt.get, "Array is empty (" + o.length(r) + ")")

    assert(1 == o.unshift(r, r.NativeObjects.getInt(2)).unboxToInt.get, "unshift single item onto empty array")

    val array = o.getNativeValue
    assert(array(0).unboxToInt.get === 2)

    assert(2 == o.unshift(r, r.NativeObjects.getInt(4)).unboxToInt.get, "unshift single item onto single-item array")

    assert(4 == o.unshift(r, r.NativeObjects.getInt(6), r.NativeObjects.getInt(8)).unboxToInt.get, "unshift two items onto array")
    assert(array(0).unboxToInt.get === 6)
    assert(array(array.length - 1).unboxToInt.get === 2)
  }

}
