package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeClassTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  test("... test MRO") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass(name = "Bar", superclass = Some(Foo))
    val Baz = new MoeClass(name = "Baz", superclass = Some(Bar))

    val mro = Baz.getMRO

    assert(mro.length == 3)

    assert(mro(0) === Baz)
    assert(mro(1) === Bar)
    assert(mro(2) === Foo)
  }

  test("... test isClassOf[String]") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass(name = "Bar", superclass = Some(Foo))
    val Baz = new MoeClass(name = "Baz", superclass = Some(Bar))

    assert(Foo.isClassOf("Foo"))
    assert(Bar.isClassOf("Foo"))
    assert(Baz.isClassOf("Foo"))

    assert(!Foo.isClassOf("Bar"))
    assert(Bar.isClassOf("Bar"))
    assert(Baz.isClassOf("Bar"))

    assert(!Foo.isClassOf("Baz"))
    assert(!Bar.isClassOf("Baz"))
    assert(Baz.isClassOf("Baz"))
  }

  test("... test isClassOf[MoeClass]") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass(name = "Bar", superclass = Some(Foo))
    val Baz = new MoeClass(name = "Baz", superclass = Some(Bar))

    assert(Foo.isClassOf(Foo))
    assert(Bar.isClassOf(Foo))
    assert(Baz.isClassOf(Foo))

    assert(!Foo.isClassOf(Bar))
    assert(Bar.isClassOf(Bar))
    assert(Baz.isClassOf(Bar))

    assert(!Foo.isClassOf(Baz))
    assert(!Bar.isClassOf(Baz))
    assert(Baz.isClassOf(Baz))
  }

  test("... test the whole thing together") {
    val klass = new MoeClass(
      name      = "TestClass",
      version   = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val ident = new MoeMethod("ident", (inv, args) => inv)
    klass.addMethod(ident)
    var obj = new MoeObject(Some(klass))
    assert(obj.isInstanceOf(klass))
    assert(obj.isInstanceOf("TestClass"))
    assert(obj.callMethod(ident) === obj)
  }

  test("... test the whole thing together (newInstance version)") {
    val klass = new MoeClass(
      name      = "TestClass",
      version   = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val ident = new MoeMethod("ident", (inv, args) => inv)
    klass.addMethod(ident)
    var obj = klass.newInstance
    assert(obj.isInstanceOf(klass))
    assert(obj.isInstanceOf("TestClass"))
    assert(obj.callMethod(ident) === obj)
  }

  test("... test method resolution") {
    val parent = new MoeClass(
      name      = "ParentClass",
      version   = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val child = new MoeClass(
      name       = "ChildClass",
      version    = Some("0.01"),
      authority  = Some("cpan:STEVAN"),
      superclass = Some(parent)
    )
    val grandchild = new MoeClass(
      name       = "GrandChildClass",
      version    = Some("0.01"),
      authority  = Some("cpan:STEVAN"),
      superclass = Some(child)
    )

    val ident = new MoeMethod("ident", (inv, args) => inv)
    parent.addMethod(ident)

    var dad      = parent.newInstance
    var son      = child.newInstance
    var grandson = grandchild.newInstance

    assert(parent.hasMethod("ident"))
    assert(child.hasMethod("ident"))
    assert(grandchild.hasMethod("ident"))

    parent.getMethod("ident") should be (Some(ident))
    child.getMethod("ident") should be (Some(ident))
    grandchild.getMethod("ident") should be (Some(ident))

    assert(dad.callMethod(ident) === dad)
    assert(son.callMethod(ident) === son)
    assert(grandson.callMethod(ident) === grandson)

    assert(dad.isInstanceOf(parent))
    assert(son.isInstanceOf(parent))
    assert(grandson.isInstanceOf(parent))

    assert(!dad.isInstanceOf(child))
    assert(son.isInstanceOf(child))
    assert(grandson.isInstanceOf(child))

    assert(!dad.isInstanceOf(grandchild))
    assert(!son.isInstanceOf(grandchild))
    assert(grandson.isInstanceOf(grandchild))
  }

  test("... test attribute resolution") {
    val parent = new MoeClass(
      name      = "ParentClass",
      version   = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val child = new MoeClass(
      name       = "ChildClass",
      version    = Some("0.01"),
      authority  = Some("cpan:STEVAN"),
      superclass = Some(parent)
    )

    val default = new MoeObject()
    val attr = new MoeAttribute("name", Some(default))
    parent.addAttribute(attr)

    assert(parent.hasAttribute("name"))
    assert(child.hasAttribute("name"))

    parent.getAttribute("name") should be (Some(attr))
    child.getAttribute("name") should be (Some(attr))
  }
}
