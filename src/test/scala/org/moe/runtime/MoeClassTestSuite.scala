package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeClassTestSuite extends FunSuite with BeforeAndAfter {

  test("... test MRO") {
    val Foo = new MoeClass("Foo")
    val Bar = new MoeClass("Bar", Some(Foo))
    val Baz = new MoeClass("Baz", Some(Bar))

    val mro = Baz.getMRO

    assert(mro.length == 3)

    assert(mro(0) === Baz)
    assert(mro(1) === Bar)
    assert(mro(2) === Foo)
  }

  test("... test the whole thing together") {
    val klass = new MoeClass("TestClass", "0.01", "cpan:STEVAN")
    klass.addMethod(new MoeMethod("ident", (inv, args) => inv))
    var obj = new MoeObject(Some(klass))
    assert(obj.callMethod("ident") === obj)
  }

  test("... test the whole thing together (newInstance version)") {
    val klass = new MoeClass("TestClass", "0.01", "cpan:STEVAN")
    klass.addMethod(new MoeMethod("ident", (inv, args) => inv))
    var obj = klass.newInstance
    assert(obj.callMethod("ident") === obj)
  }

  test("... test method resolution") {
    val parent = new MoeClass("ParentClass", "0.01", "cpan:STEVAN")
    val child = new MoeClass("ChildClass", "0.01", "cpan:STEVAN", Some(parent))

    val method = new MoeMethod("ident", (inv, args) => inv)
    parent.addMethod(method)

    var dad = parent.newInstance
    var son = child.newInstance

    assert(parent.hasMethod("ident"))
    assert(child.hasMethod("ident"))

    assert(parent.getMethod("ident") === method)
    assert(child.getMethod("ident") === method)

    assert(dad.callMethod("ident") === dad)
    assert(son.callMethod("ident") === son)
  }

  test("... test attribute resolution") {
    val parent = new MoeClass("ParentClass", "0.01", "cpan:STEVAN")
    val child = new MoeClass("ChildClass", "0.01", "cpan:STEVAN", Some(parent))

    val default = new MoeObject()
    val attr = new MoeAttribute("name", default)
    parent.addAttribute(attr)

    assert(parent.hasAttribute("name"))
    assert(child.hasAttribute("name"))

    assert(parent.getAttribute("name") === attr)
    assert(child.getAttribute("name") === attr)
  }

}
