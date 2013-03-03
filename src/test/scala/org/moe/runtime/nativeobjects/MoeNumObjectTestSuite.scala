package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNumObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Float object") {
    val o = r.NativeObjects.getNum(10.5)
    assert(o.getNativeValue === 10.5)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Float object with class") {
    val o = r.NativeObjects.getNum(10.5)
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Num").get)
  }

  test("... false Float object") {
    val o = r.NativeObjects.getNum(0.0)
    assert(o.getNativeValue === 0.0)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }


  test("... increment") {
    val o = r.NativeObjects.getNum(0.5)
    o.increment(r)
    assert(o.unboxToDouble.get === 1.5)
  }

  test("... decrement") {
    val o = r.NativeObjects.getNum(2.3)
    o.decrement(r)
    assert(o.unboxToDouble.get === 1.2999999999999998)
  }

  test("... add") {
    val o = r.NativeObjects.getNum(2.5)
    val x = o.add(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 5.0)
    assert(o.unboxToDouble.get === 2.5)
  }

  test("... subtract") {
    val o = r.NativeObjects.getNum(3.3)
    val x = o.subtract(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.7999999999999998)
    assert(o.unboxToDouble.get === 3.3)
  }

  test("... multiply") {
    val o = r.NativeObjects.getNum(2.2)
    val x = o.multiply(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 5.5)
    assert(o.unboxToDouble.get === 2.2)
  }

  test("... divide") {
    val o = r.NativeObjects.getNum(4.3)
    val x = o.divide(r, r.NativeObjects.getNum(5.1))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.8431372549019608 )
    assert(o.unboxToDouble.get === 4.3)
  }

  test("... modulo") {
    val o = r.NativeObjects.getNum(10.5)
    val x = o.modulo(r, r.NativeObjects.getNum(3.3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToDouble.get === 1)
    assert(o.unboxToDouble.get === 10.5)
  }

  test("... pow") {
    val o = r.NativeObjects.getNum(10.4)
    val x = o.pow(r, r.NativeObjects.getNum(3.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 3627.5773999128405)
    assert(o.unboxToDouble.get === 10.4)
  } 

  // equal_to 

  test("... equal_to (true)") {
    val o = r.NativeObjects.getNum(10.5)
    val x = o.equal_to(r, r.NativeObjects.getNum(10.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }

  test("... equal_to (false)") {
    val o = r.NativeObjects.getNum(10.5)
    val x = o.equal_to(r, r.NativeObjects.getNum(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  } 

  // not_equal_to

  test("... not_equal_to (false)") {
    val o = r.NativeObjects.getNum(10.2)
    val x = o.not_equal_to(r, r.NativeObjects.getNum(10.2))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  }

  test("... not_equal_to (true)") {
    val o = r.NativeObjects.getNum(10.3)
    val x = o.not_equal_to(r, r.NativeObjects.getNum(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  } 

}
