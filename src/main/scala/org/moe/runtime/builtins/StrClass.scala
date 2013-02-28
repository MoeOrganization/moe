package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Str
  */
object StrClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val strClass = r.getCoreClassFor("Str").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Str")
    )

    // MRO: Str, Scalar, Any, Object

    import r.NativeObjects._

    // increment/decrement

    strClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeStrObject]
            inv.increment(r)
            inv
        }
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "postfix:<++>",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeStrObject]
            val old = getStr(inv.getNativeValue)
            inv.increment(r)
            old
        }
      )
    )

    // concatenation

    strClass.addMethod(
      new MoeMethod(
        "infix:<.>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].concat(r, e.get("$other").get)
      )
    )

    /**
     * List of Operators to support:
     * - infix:<.>
     * - infix:<x>
     *
     * List of Methods to support:
     * - chomp
     * - chop
     * - lc
     * - lcfirst
     * - uc
     * - ucfirst
     * - length
     * - reverse
     * - index ($substring, ?$position)
     * - rindex ($substring, ?$position)
     * - sprintf ($format, @items)
     * - substr ($offset, ?$length)
     * - split ($string)
     * - concat ($string | @strings)
     * - quotemeta
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Str.pm
     */

  }

}
