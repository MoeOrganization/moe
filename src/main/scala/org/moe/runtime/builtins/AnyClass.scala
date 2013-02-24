package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Any
  */
object AnyClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val anyClass = r.getCoreClassFor("Any").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Any")
    )

    // MRO: Any, Object

    /**
     * List of Operators to support:
     # NOTE: these next 3 cause boolification
     * - infix:<&&> 
     * - infix:<||>
     * - prefix:<!>
     *
     * List of Methods to support:
     * - defined
     # NOTE: these next 3 cause stringification
     * - say
     * - print
     * - warn
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Any.pm
     */
  }
}
