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
    assert(!o.hasAssociatedType)
  }

  test("... add an associatedClass") {
    val c = new MoeClass("Test")
    val t = MoeScalarType(Some(c))
    o.setAssociatedType(Some(t))
    assert(o.hasAssociatedClass)
    assert(o.hasAssociatedType)
    o.getAssociatedClass should be (Some(c))
    o.getAssociatedType should be (Some(t))
  }

  test("... test isInstanceOf[String]") {
    val c = new MoeClass("Test")
    val t = MoeScalarType(Some(c))
    o.setAssociatedType(Some(t))
    assert(o.hasAssociatedClass)
    assert(o.hasAssociatedType)
    o.getAssociatedClass should be (Some(c))
    o.getAssociatedType should be (Some(t))
    assert(o.isInstanceOf("Test"))
  }

  test("... test isInstanceOf[MoeClass]") {
    val c = new MoeClass("Test")
    val t = MoeScalarType(Some(c))
    o.setAssociatedType(Some(t))
    assert(o.hasAssociatedClass)
    assert(o.hasAssociatedType)
    o.getAssociatedClass should be (Some(c))
    o.getAssociatedType should be (Some(t))
    assert(o.isInstanceOf(c))
  }

  test("... test isInstanceOf[String] on deep hierarchy") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass(name = "Bar", superclass = Some(Foo))
    val Baz = new MoeClass(name = "Baz", superclass = Some(Bar))

    o.setAssociatedType(Some(MoeScalarType(Some(Baz))))
    assert(o.hasAssociatedClass)
    o.getAssociatedClass should be (Some(Baz))

    assert(o.isInstanceOf("Foo")) 
    assert(o.isInstanceOf("Bar")) 
    assert(o.isInstanceOf("Baz")) 
  }

}
