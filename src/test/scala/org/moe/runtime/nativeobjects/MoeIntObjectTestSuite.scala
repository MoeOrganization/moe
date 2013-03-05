package org.moe.runtime.nativeobjects

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
    val o = r.NativeObjects.getInt(10)
    assert(o.getNativeValue === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Int object with class") {
    val o = r.NativeObjects.getInt(10)
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Int").get)
  }

  test("... false Int object") {
    val o = r.NativeObjects.getInt(0)
    assert(o.getNativeValue === 0)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  // arithmetic

  test("... increment") {
    val o = r.NativeObjects.getInt(0)
    o.increment(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... decrement") {
    val o = r.NativeObjects.getInt(2)
    o.decrement(r)
    assert(o.unboxToInt.get === 1)
  }

  test("... add") {
    val o = r.NativeObjects.getInt(2)
    val x = o.add(r, r.NativeObjects.getInt(2))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 4)
    assert(o.unboxToInt.get === 2)
  }

  test("... add w/ num") {
    val o = r.NativeObjects.getInt(2)
    val x = o.add(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 4.5)
    assert(o.unboxToInt.get === 2)
  }

  test("... subtract") {
    val o = r.NativeObjects.getInt(3)
    val x = o.subtract(r, r.NativeObjects.getInt(2))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 3)
  }

  test("... subtract w/ num") {
    val o = r.NativeObjects.getInt(3)
    val x = o.subtract(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.5)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply") {
    val o = r.NativeObjects.getInt(3)
    val x = o.multiply(r, r.NativeObjects.getInt(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 9)
    assert(o.unboxToInt.get === 3)
  }

  test("... multiply w/ Num") {
    val o = r.NativeObjects.getInt(2)
    val x = o.multiply(r, r.NativeObjects.getNum(2.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 5.0)
    assert(o.unboxToInt.get === 2)
  }

  test("... divide") {
    val o = r.NativeObjects.getInt(4)
    val x = o.divide(r, r.NativeObjects.getInt(2))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 2)
    assert(o.unboxToInt.get === 4)
  }

  test("... divide w/ Num") {
    val o = r.NativeObjects.getInt(4)
    val x = o.divide(r, r.NativeObjects.getNum(5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 0.8)
    assert(o.unboxToInt.get === 4)
  }

  test("... modulo") {
    val o = r.NativeObjects.getInt(10)
    val x = o.modulo(r, r.NativeObjects.getInt(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1)
    assert(o.unboxToInt.get === 10)
  }

  test("... pow") {
    val o = r.NativeObjects.getInt(10)
    val x = o.pow(r, r.NativeObjects.getInt(3))
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 1000)
    assert(o.unboxToInt.get === 10)
  }  

  test("... pow w/ Num") {
    val o = r.NativeObjects.getInt(10)
    val x = o.pow(r, r.NativeObjects.getNum(3.5))
    assert(x.isInstanceOf("Num"))
    assert(x.unboxToDouble.get === 3162.2776601683795)
    assert(o.unboxToInt.get === 10)
  } 

  // equal_to 

  test("... equal_to (true)") {
    val o = r.NativeObjects.getInt(10)
    val x = o.equal_to(r, r.NativeObjects.getInt(10))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }

  test("... equal_to (true) w/ Num") {
    val o = r.NativeObjects.getInt(10)
    val x = o.equal_to(r, r.NativeObjects.getNum(10.0))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }

  test("... equal_to (false)") {
    val o = r.NativeObjects.getInt(10)
    val x = o.equal_to(r, r.NativeObjects.getInt(5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  } 

  test("... equal_to (false) w/ Num") {
    val o = r.NativeObjects.getInt(10)
    val x = o.equal_to(r, r.NativeObjects.getNum(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  } 

  // not_equal_to

  test("... not_equal_to (false)") {
    val o = r.NativeObjects.getInt(10)
    val x = o.not_equal_to(r, r.NativeObjects.getInt(10))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  }

  test("... not_equal_to (false) w/ Num") {
    val o = r.NativeObjects.getInt(10)
    val x = o.not_equal_to(r, r.NativeObjects.getNum(10.0))
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
  }

  test("... not_equal_to (true)") {
    val o = r.NativeObjects.getInt(10)
    val x = o.not_equal_to(r, r.NativeObjects.getInt(5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  } 

  test("... not_equal_to (true) w/ Num") {
    val o = r.NativeObjects.getInt(10)
    val x = o.not_equal_to(r, r.NativeObjects.getNum(5.5))
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
  }   

}
