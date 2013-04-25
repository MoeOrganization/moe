package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Bool
  */
object BoolClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val boolClass = r.getCoreClassFor("Bool").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Bool")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeBoolObject = e.getCurrentInvocantAs[MoeBoolObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Bool, Scalar, Any, Object

    import r.NativeObjects._

    // equality operators

    boolClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).equal_to(r, e.get("$other").get)
      )
    )

    boolClass.addMethod(
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
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Bool.pm
     */
  }

}
