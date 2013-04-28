package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

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

    def self(e: MoeEnvironment): MoeUndefObject = e.getCurrentInvocantAs[MoeUndefObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Undef, Any, Object

    undefClass.addSubMethod(
      new MoeMethod(
        "CREATE",
        new MoeSignature(),
        env,
        (e) => getUndef
      )
    )

    // equality operators

    undefClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).equal_to(r, e.get("$other").get)
      )
    )

    undefClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).not_equal_to(r, e.get("$other").get)
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
