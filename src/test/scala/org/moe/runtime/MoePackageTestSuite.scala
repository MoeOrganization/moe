package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoePackageTestSuite extends FunSuite with BeforeAndAfter {

  var pkg : MoePackage = _

  before {
    pkg = new MoePackage("main", new MoeEnvironment())
  }

  test("... basic package") {
    assert(pkg.getName === "main")
    assert(pkg.isRoot)
    assert(pkg.getParent == None)
  }

  test("... basic package w/ subroutine") {
    pkg.addSubroutine(new MoeSubroutine("ident", (args) => args(0)))
    assert(pkg.hasSubroutine("ident"))
  }

  test("... basic package w/ class") {
    pkg.addClass(new MoeClass("Foo"))
    assert(pkg.hasClass("Foo"))
  }

  test("... basic package w/ sub-package") {
    pkg.addSubPackage(new MoePackage("Foo", new MoeEnvironment()))
    assert(pkg.hasSubPackage("Foo"))
  }

}