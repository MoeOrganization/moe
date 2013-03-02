package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeStrObjectTestSuite extends FunSuite with BeforeAndAfter {

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

  test("... chomp method w/ true return") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo\n")
    val result = o.chomp(r)
    assert(result.isTrue)
    assert(o.unboxToString.get === "foo")
  }

  test("... chomp method w/ false return") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo")
    val result = o.chomp(r)
    assert(result.isFalse)
    assert(o.unboxToString.get === "foo")
  }

  test("... chop method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo")
    val result = o.chop(r)
    assert(result.unboxToString.get === "o")
    assert(o.unboxToString.get === "fo")
  }

  test("... uc method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo")
    val result = o.uc(r)
    assert(result.unboxToString.get === "FOO")
    assert(o.unboxToString.get === "foo")
  }

  test("... lc method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("FOO")
    val result = o.lc(r)
    assert(result.unboxToString.get === "foo")
    assert(o.unboxToString.get === "FOO")
  }

  test("... ucfirst method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo")
    val result = o.ucfirst(r)
    assert(result.unboxToString.get === "Foo")
    assert(o.unboxToString.get === "foo")
  }

  test("... lcfirst method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("Foo")
    val result = o.lcfirst(r)
    assert(result.unboxToString.get === "foo")
    assert(o.unboxToString.get === "Foo")
  }

  test("... length method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("Foo")
    val result = o.length(r)
    assert(result.unboxToInt.get === 3)
    assert(o.unboxToString.get === "Foo")
  }

  test("... reverse method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("Foo")
    val result = o.reverse(r)
    assert(result.unboxToString.get === "ooF")
    assert(o.unboxToString.get === "Foo")
  }

  test("... concat method w/ single arg") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("Foo")
    val result = o.concat(r, new MoeStrObject("Bar"))
    assert(result.unboxToString.get === "FooBar")
    assert(o.unboxToString.get === "Foo")
  }

  test("... concat method w/ multiple arg") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("Foo")
    val result = o.concat(r, 
      new MoeArrayObject(List(
        new MoeStrObject("Bar"), 
        new MoeStrObject("Baz")
      ))
    )
    assert(result.unboxToString.get === "FooBarBaz")
    assert(o.unboxToString.get === "Foo")
  }

  test("... split method") {
    val r = new MoeRuntime()
    val o = new MoeStrObject("foo,bar,baz")
    val result = o.split(r, new MoeStrObject(","))

    val results = result.unboxToList.get

    assert(results(0).unboxToString.get === "foo")
    assert(results(1).unboxToString.get === "bar")
    assert(results(2).unboxToString.get === "baz")
    assert(o.unboxToString.get === "foo,bar,baz")
  }

}
