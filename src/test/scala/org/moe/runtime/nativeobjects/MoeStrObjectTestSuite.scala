package org.moe.runtime.nativeobjects

import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeStrObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple String object") {
    val o = r.NativeObjects.getStr("Hello World")
    assert(o.getNativeValue === "Hello World")
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple String object with class") {
    val o = r.NativeObjects.getStr("Hello World")
    assert(o.getAssociatedClass.get === r.getCoreClassFor("Str").get)
  }

  test("... false String object - empty string") {
    val o = r.NativeObjects.getStr("")
    assert(o.getNativeValue === "")
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... false String object - zero") {
    val o = r.NativeObjects.getStr("0")
    assert(o.getNativeValue === "0")
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... false String object - zerozero") {
    val o = r.NativeObjects.getStr("00")
    assert(o.getNativeValue === "00")
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... chomp method w/ true return") {
    val o = r.NativeObjects.getStr("foo\n")
    val x = o.chomp(r)
    assert(x.isInstanceOf("Bool"))
    assert(x.isTrue)
    assert(o.unboxToString.get === "foo")
  }

  test("... chomp method w/ false return") {
    val o = r.NativeObjects.getStr("foo")
    val x = o.chomp(r)
    assert(x.isInstanceOf("Bool"))
    assert(x.isFalse)
    assert(o.unboxToString.get === "foo")
  }

  test("... chop method") {
    val o = r.NativeObjects.getStr("foo")
    val x = o.chop(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "o")
    assert(o.unboxToString.get === "fo")
  }

  test("... uc method") {
    val o = r.NativeObjects.getStr("foo")
    val x = o.uc(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "FOO")
    assert(o.unboxToString.get === "foo")
  }

  test("... lc method") {
    val o = r.NativeObjects.getStr("FOO")
    val x = o.lc(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "foo")
    assert(o.unboxToString.get === "FOO")
  }

  test("... ucfirst method") {
    val o = r.NativeObjects.getStr("foo")
    val x = o.ucfirst(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "Foo")
    assert(o.unboxToString.get === "foo")
  }

  test("... lcfirst method") {
    val o = r.NativeObjects.getStr("Foo")
    val x = o.lcfirst(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "foo")
    assert(o.unboxToString.get === "Foo")
  }

  test("... length method") {
    val o = r.NativeObjects.getStr("Foo")
    val x = o.length(r)
    assert(x.isInstanceOf("Int"))
    assert(x.unboxToInt.get === 3)
    assert(o.unboxToString.get === "Foo")
  }

  test("... reverse method") {
    val o = r.NativeObjects.getStr("Foo")
    val x = o.reverse(r)
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "ooF")
    assert(o.unboxToString.get === "Foo")
  }

  test("... concat method w/ single arg") {
    val o = r.NativeObjects.getStr("Foo")
    val x = o.concat(r, r.NativeObjects.getStr("Bar"))
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "FooBar")
    assert(o.unboxToString.get === "Foo")
  }

  test("... concat method w/ multiple arg") {
    val o = r.NativeObjects.getStr("Foo")
    val x = o.concat(r, 
      new MoeArrayObject(List(
        r.NativeObjects.getStr("Bar"), 
        r.NativeObjects.getStr("Baz")
      ))
    )
    assert(x.isInstanceOf("Str"))
    assert(x.unboxToString.get === "FooBarBaz")
    assert(o.unboxToString.get === "Foo")
  }

  test("... split method") {
    val o = r.NativeObjects.getStr("foo,bar,baz")
    val x = o.split(r, r.NativeObjects.getStr(","))

    assert(x.isInstanceOf("Array"))

    val xs = x.unboxToArrayBuffer.get
    assert(xs(0).unboxToString.get === "foo")
    assert(xs(0).isInstanceOf("Str"))
    assert(xs(1).unboxToString.get === "bar")
    assert(xs(1).isInstanceOf("Str"))
    assert(xs(2).unboxToString.get === "baz")
    assert(xs(2).isInstanceOf("Str"))
    assert(o.unboxToString.get === "foo,bar,baz")
  }

}
