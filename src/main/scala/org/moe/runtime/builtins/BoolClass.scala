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

    // moved to coercion section

    // boolClass.addMethod(
    //   new MoeMethod(
    //     "prefix:<+>",
    //     new MoeSignature(),
    //     env,
    //     (e) => getInt(self(e).unboxToInt.getOrElse(0))
    //   )
    // )

    // coercion

    boolClass.addMethod(
      new MoeMethod(
        "Int",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeIntContext()))
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "prefix:<+>",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeIntContext()))
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "Num",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeNumContext()))
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "Bool",
        new MoeSignature(),
        env,
        (e) => self(e)
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "prefix:<?>",
        new MoeSignature(),
        env,
        (e) => self(e)
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "Str",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeStrContext()))
      )
    )

    boolClass.addMethod(
      new MoeMethod(
        "prefix:<.>",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeStrContext()))
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
