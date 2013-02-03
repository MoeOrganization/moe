package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeObjectTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  var o : MoeObject = _

  before {
    o = new MoeObject()
  }

  test("... MoeObjects do not have a default class") {
    assert(!o.hasAssociatedClass)
  }

  test("... add an associatedClass") {
    val c = new MoeClass("Test")
    o.setAssociatedClass(Some(c))
    assert(o.hasAssociatedClass)
    o.getAssociatedClass should be (Some(c))
  }

  test("... test isInstanceOf[String]") {
    val c = new MoeClass("Test")
    o.setAssociatedClass(Some(c))
    assert(o.hasAssociatedClass)
    o.getAssociatedClass should be (Some(c))
    assert(o.isInstanceOf("Test"))
  }

  test("... test isInstanceOf[MoeClass]") {
    val c = new MoeClass("Test")
    o.setAssociatedClass(Some(c))
    assert(o.hasAssociatedClass)
    o.getAssociatedClass should be (Some(c))
    assert(o.isInstanceOf(c))
  }

  test("... test isInstanceOf[String] on deep hierarchy") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass(name = "Bar", superclass = Some(Foo))
    val Baz = new MoeClass(name = "Baz", superclass = Some(Bar))

    o.setAssociatedClass(Some(Baz))
    assert(o.hasAssociatedClass)
    o.getAssociatedClass should be (Some(Baz))

    assert(o.isInstanceOf("Foo")) 
    assert(o.isInstanceOf("Bar")) 
    assert(o.isInstanceOf("Baz")) 
  }

}
