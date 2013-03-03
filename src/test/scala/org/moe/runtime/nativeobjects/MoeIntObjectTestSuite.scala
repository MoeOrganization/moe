package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeIntObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

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

  // arithmetic

  test("... increment") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(0)
    val x = o.increment(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... decrement") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(2)
    o.decrement(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... add") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(2)
    val x = o.add(r, new MoeIntObject(2))
    assert(x.unboxToInt.get === 4)
    assert(o.unboxToInt.get === 2)
  }

  test("... add w/ num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(2)
    val x = o.add(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 4.5)
    assert(o.unboxToInt.get === 2)
  }

  test("... subtract") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(3)
    val x = o.subtract(r, new MoeIntObject(2))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 3)
  }

  test("... subtract w/ num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(3)
    val x = o.subtract(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 0.5)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(3)
    val x = o.multiply(r, new MoeIntObject(3))
    assert(x.unboxToInt.get === 9)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(2)
    val x = o.multiply(r, new MoeNumObject(2.5))
    assert(x.unboxToDouble.get === 5.0)
    assert(o.unboxToInt.get === 2)
  }

  test("... divide") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(4)
    val x = o.divide(r, new MoeIntObject(2))
    assert(x.unboxToDouble.get === 2)
    assert(o.unboxToInt.get === 4)
  }

  test("... divide w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(4)
    val x = o.divide(r, new MoeNumObject(5))
    assert(x.unboxToDouble.get === 0.8)
    assert(o.unboxToInt.get === 4)
  }

  test("... modulo") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.modulo(r, new MoeIntObject(3))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 10)
  }

  test("... pow") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.pow(r, new MoeIntObject(3))
    assert(x.unboxToInt.get === 1000)
    assert(o.unboxToInt.get === 10)
  }  

  test("... pow w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.pow(r, new MoeNumObject(3.5))
    assert(x.unboxToInt.get === 3162)
    assert(o.unboxToInt.get === 10)
  } 

  // equal_to 

  test("... equal_to (true)") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeIntObject(10))
    assert(x.isTrue)
  }

  test("... equal_to (true) w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeNumObject(10.0))
    assert(x.isTrue)
  }

  test("... equal_to (false)") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeIntObject(5))
    assert(x.isFalse)
  } 

  test("... equal_to (false) w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeNumObject(5.5))
    assert(x.isFalse)
  } 

  // not_equal_to

  test("... not_equal_to (false)") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeIntObject(10))
    assert(x.isFalse)
  }

  test("... not_equal_to (false) w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeNumObject(10.0))
    assert(x.isFalse)
  }

  test("... not_equal_to (true)") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeIntObject(5))
    assert(x.isTrue)
  } 

  test("... not_equal_to (true) w/ Num") {
    val r = new MoeRuntime()
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeNumObject(5.5))
    assert(x.isTrue)
  }   

}
