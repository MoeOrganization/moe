package org.moe.runtime

import org.moe.runtime.nativeobjects._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNativeObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... simple String object") {
    val o = new MoeStrObject("Hello World")
    assert(o.getNativeValue === "Hello World")
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple String object with class") {
    val c = new MoeClass("String")
    val o = new MoeStrObject("Hello World", Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false String object - empty string") {
    val o = new MoeStrObject("")
    assert(o.getNativeValue === "")
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... false String object - zero") {
    val o = new MoeStrObject("0")
    assert(o.getNativeValue === "0")
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... false String object - zerozero") {
    val o = new MoeStrObject("00")
    assert(o.getNativeValue === "00")
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
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

  test("... simple Boolean object") {
    val o = new MoeBoolObject(true)
    assert(o.getNativeValue === true)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... false Boolean object") {
    val o = new MoeBoolObject(false)
    assert(o.getNativeValue === false)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Boolean object with class") {
    val c = new MoeClass("Boolean")
    val o = new MoeBoolObject(false, Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... simple Undef object") {
    val o = new MoeUndefObject()
    assert(o.getNativeValue == null)
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(o.isUndef)
    assert(o.toString === "undef")
  }

  test("... simple Undef object with class") {
    val c = new MoeClass("Undef")
    val o = new MoeUndefObject(Some(c))
    assert(o.getAssociatedClass.get === c)
  }

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
    val nested = array(1).unboxToArray.get
    assert(nested(0).unboxToInt.get === 42)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Hash object") {
    val o = new MoeHashObject(
      Map(
        "foo" -> new MoeUndefObject(),
        "bar" -> new MoeIntObject(10)
      )
    )
    val hash = o.getNativeValue
    assert(hash("foo").isUndef)
    assert(hash("bar").unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Hash object with class") {
    val c = new MoeClass("Hash")
    val o = new MoeHashObject(Map(), Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false Hash object") {
    val o = new MoeHashObject(Map())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Hash object") {
    val o = new MoeHashObject(
      Map(
        "foo" -> new MoeArrayObject(
          List(
            new MoeUndefObject(),
            new MoeIntObject(10)
          )
        ),
        "bar" -> new MoeIntObject(10)
        )
    )
    val hash = o.getNativeValue
    assert(hash("bar").unboxToInt.get === 10)
    val nested = hash("foo").unboxToArray.get
    assert(nested(0).isUndef)
    assert(nested(1).unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Pair object") {
    val o = new MoePairObject("foo" -> new MoeIntObject(10))
    val pair = o.getNativeValue
    assert(pair._1 === "foo")
    assert(pair._2.unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... test simple native object dump output") {
    assert((new MoeIntObject(42)).toString == "42")
    assert((new MoeIntObject(-42)).toString == "-42")

    assert((new MoeNumObject(0.42)).toString == "0.42")
    assert((new MoeNumObject(-0.42)).toString == "-0.42")

    assert((new MoeBoolObject(true)).toString == "true")
    assert((new MoeBoolObject(false)).toString == "false")

    assert((new MoeStrObject("jason")).toString == "\"jason\"")
  }

  test("... test complex native object dump output") {
    val array = new MoeArrayObject(
      List(
        new MoeIntObject(42),
        new MoeNumObject(98.6),
        new MoeStrObject("moe")
      )
    )
    assert(array.toString == """[42, 98.6, "moe"]""")

    val hash = new MoeHashObject(
      Map(
        "name" -> new MoeStrObject("moe"),
        "awesome" -> new MoeBoolObject(true)
      )
    )
    assert(
      hash.toString ==
        """{name => "moe", awesome => true}"""
    )

    val hashInArray = new MoeArrayObject(
      List(
        new MoeIntObject(10),
        new MoeIntObject(20),
        hash
      )
    )
    assert(
      hashInArray.toString ==
        """[10, 20, {name => "moe", awesome => true}]"""
    )

    val arrayInArray = new MoeArrayObject(
      List(
        new MoeIntObject(10),
        new MoeIntObject(20),
        array
      )
    )
    assert(arrayInArray.toString == """[10, 20, [42, 98.6, "moe"]]""")

    val arrayInHash = new MoeHashObject(
      Map("my_hash" -> array)
    )
    assert(arrayInHash.toString == """{my_hash => [42, 98.6, "moe"]}""")

    val hashInHash = new MoeHashObject(
      Map("my_hash" -> hash)
    )
    assert(
      hashInHash.toString ==
        """{my_hash => {name => "moe", awesome => true}}"""
    )
  }


}
