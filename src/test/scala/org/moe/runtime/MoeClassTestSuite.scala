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

  test("... test the whole thing together") {
    val klass = new MoeClass(
      name = "TestClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    klass.addMethod(new MoeMethod("ident", (inv, args) => inv))
    var obj = new MoeObject(Some(klass))
    assert(obj.callMethod("ident") === obj)
  }

  test("... test the whole thing together (newInstance version)") {
    val klass = new MoeClass(
      name = "TestClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    klass.addMethod(new MoeMethod("ident", (inv, args) => inv))
    var obj = klass.newInstance
    assert(obj.callMethod("ident") === obj)
  }

  test("... test method resolution") {
    val parent = new MoeClass(
      name = "ParentClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val child = new MoeClass(
      name = "ChildClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN"),
      superclass = Some(parent)
    )

    val method = new MoeMethod("ident", (inv, args) => inv)
    parent.addMethod(method)

    var dad = parent.newInstance
    var son = child.newInstance

    assert(parent.hasMethod("ident"))
    assert(child.hasMethod("ident"))

    parent.getMethod("ident") should be (Some(method))
    child.getMethod("ident") should be (Some(method))

    assert(dad.callMethod("ident") === dad)
    assert(son.callMethod("ident") === son)

    intercept[Runtime.Errors.MethodNotFound] {
      son.callMethod("foobar", List())
    }
  }

  test("... test attribute resolution") {
    val parent = new MoeClass(
      name = "ParentClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN")
    )
    val child = new MoeClass(
      name = "ChildClass",
      version = Some("0.01"),
      authority = Some("cpan:STEVAN"),
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
