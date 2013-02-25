package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Undef
  */
object UndefClass {

  def apply(r: MoeRuntime): Unit = {
    val env        = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val undefClass = r.getCoreClassFor("Undef").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Undef")
    )

    // MRO: Undef, Scalar, Any, Object

    /**
     * List of Methods to support:
     * 
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/.pm
     */

  }

}
