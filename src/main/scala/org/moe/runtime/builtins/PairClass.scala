package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Pair
  */
object PairClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val pairClass = r.getCoreClassFor("Pair").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Pair")
    )

    // MRO: Pair, Any, Object

    /**
     * List of Methods to support:
     * 
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/.pm
     */

  }

}
