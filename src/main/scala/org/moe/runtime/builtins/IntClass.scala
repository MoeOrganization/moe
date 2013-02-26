package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter.InterpreterUtils._

/**
  * setup class Int 
  */
object IntClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val intClass = r.getCoreClassFor("Int").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Int")
    )

    // MRO: Int, Scalar, Any, Object

    import r.NativeObjects._

    // increment/decrement

    intClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeIntObject]
            inv.increment(r)
            inv
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "postfix:<++>",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeIntObject]
            val old = getInt(inv.getNativeValue)
            inv.increment(r)
            old
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "prefix:<-->",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeIntObject]
            inv.decrement(r)
            inv
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "postfix:<-->",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = e.getCurrentInvocant.get.asInstanceOf[MoeIntObject]
            val old = getInt(inv.getNativeValue)
            inv.decrement(r)
            old
        }
      )
    )

    // arithmetic
    
    intClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].add(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].subtract(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].multiply(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].divide(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].modulo(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].pow(r, e.get("$other").get)
      )
    )

    // relational operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].less_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].greater_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].not_equal_to(r, e.get("$other").get)
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
