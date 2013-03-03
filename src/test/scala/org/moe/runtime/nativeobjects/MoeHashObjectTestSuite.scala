package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeHashObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Hash object") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getUndef,
        "bar" -> r.NativeObjects.getInt(10)
      )
    )
    val hash = o.unboxToMap.get
    assert(hash("foo").isUndef)
    assert(hash("bar").unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Hash object with class") {
    val o = r.NativeObjects.getHash(HashMap())
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Hash").get)
  }

  test("... false Hash object") {
    val o = r.NativeObjects.getHash(HashMap())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Hash object") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getArray(
          List(
            r.NativeObjects.getUndef,
            r.NativeObjects.getInt(10)
          )
        ),
        "bar" -> r.NativeObjects.getInt(10)
        )
    )
    val hash = o.unboxToMap.get
    assert(hash("bar").unboxToInt.get === 10)
    val nested = hash("foo").unboxToList.get
    assert(nested(0).isUndef)
    assert(nested(1).unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  // runtime methods

  test("... at_key") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getInt(20),
        "bar" -> r.NativeObjects.getInt(10)
        )
    )
    val x = o.at_key(r, r.NativeObjects.getStr("foo"))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 20)
  }

  test("... bind_key") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getInt(20),
        "bar" -> r.NativeObjects.getInt(10)
        )
    )
    val x1 = o.at_key(r, r.NativeObjects.getStr("foo"))
    assert(x1.isInstanceOf("Int"))
    assert(x1.unboxToInt.get === 20)

    val x2 = o.bind_key(r, r.NativeObjects.getStr("foo"), r.NativeObjects.getStr("FOO"))
    assert(x2.isInstanceOf("Str"))
    assert(x2.unboxToString.get === "FOO")

    assert(x1.getID != x2.getID)

    val x3 = o.at_key(r, r.NativeObjects.getStr("foo"))
    assert(x3.isInstanceOf("Str"))
    assert(x3.unboxToString.get === "FOO")

    assert(x2.getID === x3.getID)
  }
  
  test("... keys") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getInt(20),
        "bar" -> r.NativeObjects.getInt(10)
        )
    )
    val x = o.keys(r)
    assert(x.isInstanceOf("Array"))
    assert(x.at_pos(r, r.NativeObjects.getInt(0)).unboxToString.get === "foo")
    assert(x.at_pos(r, r.NativeObjects.getInt(1)).unboxToString.get === "bar")
  }

  test("... values") {
    val o = r.NativeObjects.getHash(
      HashMap(
        "foo" -> r.NativeObjects.getInt(20),
        "bar" -> r.NativeObjects.getInt(10)
        )
    )
    val x = o.values(r)
    assert(x.isInstanceOf("Array"))
    assert(x.at_pos(r, r.NativeObjects.getInt(0)).unboxToInt.get === 20)
    assert(x.at_pos(r, r.NativeObjects.getInt(1)).unboxToInt.get === 10)
  }

}
