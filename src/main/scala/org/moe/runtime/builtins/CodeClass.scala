package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Code
  */
object CodeClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val codeClass = r.getCoreClassFor("Code").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Code")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeCode = e.getCurrentInvocant.getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    ).asInstanceOf[MoeCode]

    // MRO: Code, Any, Object

    codeClass.addMethod(
      new MoeMethod(
        "apply",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        (e) => self(e).execute( new MoeArguments( e.get("@_").get.unboxToArrayBuffer.get.toList ) )
      )
    )

    /**
     * List of Methods to support:
     * - apply(*@args)
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/.pm
     */
  }
}
