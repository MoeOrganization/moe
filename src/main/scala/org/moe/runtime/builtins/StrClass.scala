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
            val old = getStr(inv.unboxToString.get)
            inv.increment(r)
            old
        }
      )
    )

    // operators

    // methods

    strClass.addMethod(
      new MoeMethod(
        "chomp",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].chomp(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "chop",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].chop(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "uc",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].uc(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "lc",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].lc(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "ucfirst",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].ucfirst(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "lcfirst",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].lcfirst(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "length",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].length(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "reverse",
        new MoeSignature(), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].reverse(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "split",
        new MoeSignature(List(new MoeNamedParameter("$string"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].split(
            r, 
            e.get("$string").get.asInstanceOf[MoeStrObject]
        )
      )
    )

    // FIXME
    // this needs to support variable args
    // - SL
    strClass.addMethod(
      new MoeMethod(
        "concat",
        new MoeSignature(List(new MoeSlurpyParameter("@strings"))),
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].concatAll(r, e.get("@strings").get.asInstanceOf[MoeArrayObject])
      )
    )        

    // concatenation

    strClass.addMethod(
      new MoeMethod(
        "infix:<.>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].concat(r, e.get("$other").get.asInstanceOf[MoeStrObject])
      )
    )

    // repetition

    strClass.addMethod(
      new MoeMethod(
        "infix:<x>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeStrObject].repeat(r, e.get("$other").get)
      )
    )

    /**
     * List of Operators to support:
     * - infix:<.>
     * - infix:<x>
     *
     * List of Methods to support:
     * - index ($substring, ?$position)
     * - rindex ($substring, ?$position)
     * - sprintf ($format, @items)
     * - substr ($offset, ?$length)
     * - quotemeta
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Str.pm
     */

  }

}
