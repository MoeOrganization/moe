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

    import r.NativeObjects._

    // MRO: Undef, Scalar, Any, Object

    undefClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(),
        env,
        (e) => getUndef
      )
    )

    /**
     * List of Methods to support:
     * 
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/.pm
     */

  }

}
