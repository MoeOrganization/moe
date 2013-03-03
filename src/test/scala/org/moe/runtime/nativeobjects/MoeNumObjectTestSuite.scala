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


  test("... increment") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(0.5)
    val x = o.increment(r)
    assert(o.unboxToDouble.get === 1.5)
  }

  test("... decrement") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(2.3)
    o.decrement(r)
    assert(o.unboxToDouble.get === 1.2999999999999998)
  }

  test("... add") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(2.5)
    val x = o.add(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 5.0)
    assert(o.unboxToDouble.get === 2.5)
  }

  test("... subtract") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(3.3)
    val x = o.subtract(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 0.7999999999999998)
    assert(o.unboxToDouble.get === 3.3)
  }

  test("... multiply") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(2.2)
    val x = o.multiply(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 5.5)
    assert(o.unboxToDouble.get === 2.2)
  }

  test("... divide") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(4.3)
    val x = o.divide(r, new MoeNumObject(5.1))
    assert(x.unboxToDouble.get === 0.8431372549019608 )
    assert(o.unboxToDouble.get === 4.3)
  }

  test("... modulo") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.5)
    val x = o.modulo(r, new MoeNumObject(3.3))
    assert(x.unboxToDouble.get === 1)
    assert(o.unboxToDouble.get === 10.5)
  }

  test("... pow") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.4)
    val x = o.pow(r, new MoeNumObject(3.5))
    assert(x.unboxToDouble.get === 3627.5773999128405)
    assert(o.unboxToDouble.get === 10.4)
  } 

  // equal_to 

  test("... equal_to (true)") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.5)
    val x = o.equal_to(r, new MoeNumObject(10.5))
    assert(x.isTrue)
  }

  test("... equal_to (false)") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.5)
    val x = o.equal_to(r, new MoeNumObject(5.5))
    assert(x.isFalse)
  } 

  // not_equal_to

  test("... not_equal_to (false)") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.2)
    val x = o.not_equal_to(r, new MoeNumObject(10.2))
    assert(x.isFalse)
  }

  test("... not_equal_to (true)") {
    val r = new MoeRuntime()
    val o = new MoeNumObject(10.3)
    val x = o.not_equal_to(r, new MoeNumObject(5.5))
    assert(x.isTrue)
  } 

}
