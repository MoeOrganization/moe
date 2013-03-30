package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Pair
  */
object PairClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val pairClass = r.getCoreClassFor("Pair").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Pair")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoePairObject = e.getCurrentInvocantAs[MoePairObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Pair, Any, Object

    pairClass.addMethod(
      new MoeMethod(
        "key",
        new MoeSignature(),
        env,
        (e) => self(e).key(r)
      )
    )

    pairClass.addMethod(
      new MoeMethod(
        "value",
        new MoeSignature(),
        env,
        (e) => self(e).value(r)
      )
    )

    pairClass.addMethod(
      new MoeMethod(
        "kv",
        new MoeSignature(),
        env,
        (e) => self(e).kv(r)
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
