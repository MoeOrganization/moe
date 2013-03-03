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
    val o = new MoeIntObject(0)
    o.increment(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... decrement") {
    val o = new MoeIntObject(2)
    o.decrement(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... add") {
    val o = new MoeIntObject(2)
    val x = o.add(r, new MoeIntObject(2))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 4)
    assert(o.unboxToInt.get === 2)
  }

  test("... add w/ num") {
    val o = new MoeIntObject(2)
    val x = o.add(r, new MoeNumObject(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 4.5)
    assert(o.unboxToInt.get === 2)
  }

  test("... subtract") {
    val o = new MoeIntObject(3)
    val x = o.subtract(r, new MoeIntObject(2))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 3)
  }

  test("... subtract w/ num") {
    val o = new MoeIntObject(3)
    val x = o.subtract(r, new MoeNumObject(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.5)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply") {
    val o = new MoeIntObject(3)
    val x = o.multiply(r, new MoeIntObject(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 9)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply w/ Num") {
    val o = new MoeIntObject(2)
    val x = o.multiply(r, new MoeNumObject(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 5.0)
    assert(o.unboxToInt.get === 2)
  }

  test("... divide") {
    val o = new MoeIntObject(4)
    val x = o.divide(r, new MoeIntObject(2))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 2)
    assert(o.unboxToInt.get === 4)
  }

  test("... divide w/ Num") {
    val o = new MoeIntObject(4)
    val x = o.divide(r, new MoeNumObject(5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.8)
    assert(o.unboxToInt.get === 4)
  }

  test("... modulo") {
    val o = new MoeIntObject(10)
    val x = o.modulo(r, new MoeIntObject(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 10)
  }

  test("... pow") {
    val o = new MoeIntObject(10)
    val x = o.pow(r, new MoeIntObject(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1000)
    assert(o.unboxToInt.get === 10)
  }  

  test("... pow w/ Num") {
    val o = new MoeIntObject(10)
    val x = o.pow(r, new MoeNumObject(3.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 3162.2776601683795)
    assert(o.unboxToInt.get === 10)
  } 

  // equal_to 

  test("... equal_to (true)") {
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeIntObject(10))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }

  test("... equal_to (true) w/ Num") {
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeNumObject(10.0))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }

  test("... equal_to (false)") {
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeIntObject(5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  } 

  test("... equal_to (false) w/ Num") {
    val o = new MoeIntObject(10)
    val x = o.equal_to(r, new MoeNumObject(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  } 

  // not_equal_to

  test("... not_equal_to (false)") {
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeIntObject(10))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  }

  test("... not_equal_to (false) w/ Num") {
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeNumObject(10.0))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  }

  test("... not_equal_to (true)") {
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeIntObject(5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  } 

  test("... not_equal_to (true) w/ Num") {
    val o = new MoeIntObject(10)
    val x = o.not_equal_to(r, new MoeNumObject(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }   

}
