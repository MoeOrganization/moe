package org.moe.runtime.builtins

import org.moe.runtime._

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

    // logical ops

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<!>",
        new MoeSignature(), 
        env, 
        (e) => if (e.getCurrentInvocant.get.isTrue) getFalse else getTrue
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "infix:<&&>",
        new MoeSignature(List(new MoeNamedParameter("$other"))), 
        env, 
        { (e) =>
            val inv = e.getCurrentInvocant.get
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
        new MoeSignature(List(new MoeNamedParameter("$other"))), 
        env, 
        { (e) =>
            val inv = e.getCurrentInvocant.get
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

    // MRO: Any, Object

    /**
     * List of Methods to support:
     * - defined
     # NOTE: these next 3 cause stringification
     * - say
     * - print
     * - warn
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Any.pm
     */
  }
}
