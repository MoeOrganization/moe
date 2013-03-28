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

    boolClass.addMethod(
      new MoeMethod(
        "prefix:<+>",
        new MoeSignature(),
        env,
        (e) => getInt(self(e).unboxToInt.getOrElse(0))
      )
    )

    // ternary operator

    boolClass.addMethod(
      new MoeMethod(
        "infix:<?:>",
        new MoeSignature(List(new MoeNamedParameter("$trueExpr"), new MoeNamedParameter("$falseExpr"))),
        env,
        { (e) =>
            val inv = self(e)
            if (inv.isTrue)
              e.get("$trueExpr").get match {
                case deferredExpr: MoeLazyEval => deferredExpr.eval
                case expr:         MoeObject   => expr
              }
            else
              e.get("$falseExpr").get match {
                case deferredExpr: MoeLazyEval => deferredExpr.eval
                case expr:         MoeObject   => expr
              }
        }
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
