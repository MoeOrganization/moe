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
    pkg.addSubroutine(
      new MoeSubroutine(
        name            = "ident", 
        declaration_env = new MoeEnvironment(),
        signature       = new MoeSignature(List(new MoePositionalParameter("$x"))),
        body            = (e) => e.get("$x").get
      )
    )
    assert(pkg.hasSubroutine("ident"))
  }

  test("... basic package w/ class") {
    pkg.addClass(new MoeClass("Foo"))
    assert(pkg.hasClass("Foo"))
  }

  test("... basic package w/ sub-package") {
    val subpkg = new MoePackage("Foo", new MoeEnvironment())
    assert(subpkg.isRoot)

    pkg.addSubPackage(subpkg)
    assert(!subpkg.isRoot)
    assert(subpkg.getParent.get == pkg)

    assert(pkg.hasSubPackage("Foo"))
  }

}
