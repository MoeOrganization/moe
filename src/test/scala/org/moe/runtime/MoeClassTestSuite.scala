package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeClassTestSuite extends FunSuite with BeforeAndAfter {

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

}
