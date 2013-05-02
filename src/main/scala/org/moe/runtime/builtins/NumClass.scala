package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Num 
  */
object NumClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val numClass = r.getCoreClassFor("Num").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Num")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeNumObject = e.getCurrentInvocantAs[MoeNumObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Num, Scalar, Any, Object

    numClass.addSubMethod(
      new MoeMethod(
        "CREATE",
        new MoeSignature(List(new MoeOptionalParameter("$num"))),
        env,
        (e) => e.get("$num") match {
          case Some(n: MoeNumObject) => n.copy
          case _                     => getNum(0.0)
        }
      )
    )

    // increment/decrement

    numClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(), 
        env, 
        { (e) => 
            val inv = self(e)
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
            val inv = self(e)
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
            val inv = self(e)
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
            val inv = self(e)
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
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).add(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).subtract(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).multiply(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).divide(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).modulo(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).pow(r, e.get("$other").get)
      )
    )

    // relational operators

    numClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).not_equal_to(r, e.get("$other").get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<<=>>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).compare_to(r, e.get("$other").get)
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
