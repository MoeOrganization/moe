package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Any
  */
object AnyClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val anyClass = r.getCoreClassFor("Any").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Any")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeObject = e.getCurrentInvocant.getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // output methods

    anyClass.addMethod(
      new MoeMethod(
        "say",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.say(self(e))
            getUndef
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "print",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.print(self(e))
            getUndef
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "warn",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.warn(self(e))
            getUndef
        }
      )
    )

    // logical ops

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<!>",
        new MoeSignature(), 
        env, 
        (e) => if (self(e).isTrue) getFalse else getTrue
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "infix:<&&>",
        new MoeSignature(List(new MoePositionalParameter("$other"))), 
        env, 
        { (e) =>
            val inv = self(e)
            if (inv.isFalse)
              inv
            else
              e.get("$other").get match {
                case deferred: MoeLazyEval => deferred.eval
                case other:    MoeObject   => other
              }
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "infix:<||>",
        new MoeSignature(List(new MoePositionalParameter("$other"))), 
        env, 
        { (e) =>
            val inv = self(e)
            if (inv.isTrue)
              inv
            else
              e.get("$other").get match {
                case deferred: MoeLazyEval => deferred.eval
                case other:    MoeObject   => other
              }
        }
      )
    )

    // ternary operator

    anyClass.addMethod(
      new MoeMethod(
        "infix:<?:>",
        new MoeSignature(List(new MoePositionalParameter("$trueExpr"), new MoePositionalParameter("$falseExpr"))),
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

    // MRO: Any, Object

    /**
     * List of Methods to support:
     * - defined
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Any.pm
     */
  }
}
