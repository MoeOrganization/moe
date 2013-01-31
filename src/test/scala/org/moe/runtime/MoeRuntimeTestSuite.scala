package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeRuntimeTestSuite extends FunSuite with BeforeAndAfter {

  test("... basic runtime test (unbootstrapped)") {
    assert(!MoeRuntime.isBootstrapped)
  }

  test("... basic runtime test (bootstrapped)") {
    assert(!MoeRuntime.isBootstrapped)
    MoeRuntime.bootstrap()
    assert(MoeRuntime.isBootstrapped)
  }

  test("... test core-package (basic core classes)") {
    MoeRuntime.bootstrap()
    assert(MoeRuntime.isBootstrapped)
    assert(MoeRuntime.getCorePackage.hasClass("Scalar"))
    assert(MoeRuntime.getCorePackage.hasClass("Array"))
    assert(MoeRuntime.getCorePackage.hasClass("Hash"))

    assert(MoeRuntime.getCorePackage.hasClass("Null"))
    assert(MoeRuntime.getCorePackage.hasClass("Boolean"))
    assert(MoeRuntime.getCorePackage.hasClass("Number"))
    assert(MoeRuntime.getCorePackage.hasClass("String"))
    assert(MoeRuntime.getCorePackage.hasClass("Exception"))
  }

}