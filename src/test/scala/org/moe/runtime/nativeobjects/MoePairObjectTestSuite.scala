package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoePairObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple Pair object") {
    val o = new MoePairObject("foo" -> new MoeIntObject(10))
    val pair = o.getNativeValue
    assert(pair._1 === "foo")
    assert(pair._2.unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

}
