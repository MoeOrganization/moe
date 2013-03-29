package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeAttributeTestSuite extends FunSuite with BeforeAndAfter {

  test("... basic attribute") {
    val default = new MoeObject()
    val attr    = new MoeAttribute("Foo", Some(() => default))

    assert(attr.getName == "Foo")
    assert(attr.getDefault === Some(default))
    assert(attr.hasDefault)
  }

  test("... attribute with no default") {
    val default = new MoeObject()
    val attr    = new MoeAttribute("Foo")

    assert(attr.getName == "Foo")
    assert(!attr.hasDefault)
  }
}