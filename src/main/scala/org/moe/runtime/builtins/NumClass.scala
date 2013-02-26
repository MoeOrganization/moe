package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter.InterpreterUtils._

/**
  * setup class Num 
  */
object NumClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val numClass = r.getCoreClassFor("Num").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Num")
    )

    // MRO: Num, Scalar, Any, Object

    import r.NativeObjects._

    // increment/decrement

    numClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeNumObject]
            inv.increment(r)
            inv
        }
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "postfix:<++>",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeNumObject]
            val old = getNum(inv.getNativeValue)
            inv.increment(r)
            old
        }
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "prefix:<-->",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeNumObject]
            inv.decrement(r)
            inv
        }
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "postfix:<-->",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeNumObject]
            val old = getNum(inv.getNativeValue)
            inv.decrement(r)
            old
        }
      )
    )

    // arithemtic

    numClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].add(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].subtract(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].multiply(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].divide(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].modulo(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].pow(r, e.get("$other").get)
      )
    )

    // relational operators

    numClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].less_than(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].greater_than(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeNumObject].not_equal_to(r, e.get("$other").get)
      )
    )

    /**
     * List of Operators to support:
     * - prefix:<->
     * - infix:<<=>>
     * NOTE: probably need the bitwise stuff too
     *
     * List of Methods to support:
     * - abs
     * - cos
     * - acos
     * - exp
     * - int
     * - log
     * - oct
     * - hex
     * - sin
     * - asin
     * - tan
     * - atan
     * - sqrt
     * - chr
     * - floor
     * - round
     * - ceiling
     * - exp
     * - base
     * - gcd
     * - lcm
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Real.pm
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Int.pm
     */

  }

}
