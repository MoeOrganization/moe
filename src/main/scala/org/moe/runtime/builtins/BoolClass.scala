package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Bool
  */
object BoolClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val boolClass = r.getCoreClassFor("Bool").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Bool")
    )

    // MRO: Bool, Scalar, Any, Object

    /**
     * List of Methods to support:
     * 
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Bool.pm
     */
  }

}
