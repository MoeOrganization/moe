package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeRuntimeTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  test("... basic runtime test (bootstrapped)") {
    val runtime = new MoeRuntime()
    runtime.bootstrap()
    assert(runtime.isBootstrapped)
    assert(runtime.areWarningsEnabled)
    assert(runtime.getSystem != null)
  }

  test("... basic runtime test (warnings disabled)") {
    val runtime = new MoeRuntime(warnings = false)
    runtime.bootstrap()
    assert(runtime.isBootstrapped)
    assert(!runtime.areWarningsEnabled)
    assert(runtime.getSystem != null)
  }

  test("... test root-package (basic env setup)") {
    val runtime = new MoeRuntime()
    runtime.bootstrap()
    assert(runtime.isBootstrapped)

    val rootPackage = runtime.getRootPackage
    rootPackage.getEnv should be (runtime.getRootEnv)
    runtime.getRootEnv.getCurrentPackage should be (Some(rootPackage))
  }

  test("... test root-package (basic core setup)") {
    val runtime = new MoeRuntime()
    runtime.bootstrap()
    assert(runtime.isBootstrapped)

    val rootPackage = runtime.getRootPackage
    val corePackage = runtime.getCorePackage
    rootPackage.getSubPackage("CORE") should be (Some(corePackage))
  }

  test("... test core-package (basic env setup)") {
    val runtime = new MoeRuntime()
    runtime.bootstrap()
    assert(runtime.isBootstrapped)

    val corePackage = runtime.getCorePackage
    corePackage.getEnv.getCurrentPackage should be (Some(corePackage))
    corePackage.getEnv.getParent should be (Some(runtime.getRootEnv))
  }

  test("... test core-package (basic core classes)") {
    val runtime = new MoeRuntime()
    runtime.bootstrap()
    assert(runtime.isBootstrapped)

    val corePackage = runtime.getCorePackage

    assert(corePackage.hasClass("Object"))
    assert(corePackage.hasClass("Class"))

    assert(corePackage.hasClass("Any"))
    assert(corePackage.hasClass("Scalar"))
    assert(corePackage.hasClass("Array"))
    assert(corePackage.hasClass("Hash"))

    assert(corePackage.hasClass("Undef"))
    assert(corePackage.hasClass("Bool"))
    assert(corePackage.hasClass("Num"))
    assert(corePackage.hasClass("Int"))
    assert(corePackage.hasClass("Str"))
  }

  test("... runtime warn") {
    val outStream = new java.io.ByteArrayOutputStream()
    val msg       = "HELLO WARN WORLD";
    val msgs      = Array("HELLO", "WARN", "WORLD")

    val runtime = new MoeRuntime(
      system = new MoeSystem(
        STDERR = new java.io.PrintStream(outStream)
      )
    )
    runtime.bootstrap()
    assert(runtime.isBootstrapped)
    assert(runtime.areWarningsEnabled)

    var out = msg + "\n"
    runtime.warn(msg)
    assert(outStream.toString() === out)

    out += msgs.mkString + "\n"
    runtime.warn(msgs)
    assert(outStream.toString() === out)
  }

}
