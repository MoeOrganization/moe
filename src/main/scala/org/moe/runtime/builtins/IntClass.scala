package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Int
  */
object IntClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val intClass = r.getCoreClassFor("Int").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Int")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeIntObject = e.getCurrentInvocantAs[MoeIntObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Int, Scalar, Any, Object

    intClass.addSubMethod(
      new MoeMethod(
        "CREATE",
        new MoeSignature(List(new MoeOptionalParameter("$int"))),
        env,
        (e) => e.get("$int") match {
          case Some(i: MoeIntObject) => i.copy
          case _                     => getInt(0)
        }
      )
    )

    // Unary -

    intClass.addMethod(
      new MoeMethod(
        "prefix:<->",
        new MoeSignature(),
        env,
        (e) => self(e).negate(r)
      )
    )

    // increment/decrement

    intClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(),
        env,
        (e) => self(e).increment(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "postfix:<++>",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = self(e)
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
        (e) => self(e).decrement(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "postfix:<-->",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = self(e)
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
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).add(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).subtract(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).multiply(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).divide(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).modulo(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).pow(r, e.get("$other").get)
      )
    )

    // relational operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    // equality operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).not_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).compare_to(r, e.get("$other").get)
      )
    )

    // bitwise operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<&>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).bit_and(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<|>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).bit_or(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<^>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).bit_xor(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<<>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).bit_shift_left(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).bit_shift_right(r, e.get("$other").get)
      )
    )

    // methods

    intClass.addMethod(
      new MoeMethod(
        "abs",
        new MoeSignature(),
        env,
        (e) => self(e).abs(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "sin",
        new MoeSignature(),
        env,
        (e) => self(e).sin(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "cos",
        new MoeSignature(),
        env,
        (e) => self(e).cos(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "tan",
        new MoeSignature(),
        env,
        (e) => self(e).tan(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "asin",
        new MoeSignature(),
        env,
        (e) => self(e).asin(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "acos",
        new MoeSignature(),
        env,
        (e) => self(e).acos(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "atan",
        new MoeSignature(),
        env,
        (e) => self(e).atan(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "atan2",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).atan2(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "exp",
        new MoeSignature(),
        env,
        (e) => self(e).exp(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "log",
        new MoeSignature(),
        env,
        (e) => self(e).log(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "sqrt",
        new MoeSignature(),
        env,
        (e) => self(e).sqrt(r)
      )
    )

    /**
     * List of Operators to support:
     * - prefix:<->
     * - infix:<<=>>
     * NOTE: probably need the bitwise stuff too
     *
     * List of Methods to support:
     * - int
     * - oct
     * - hex
     * - chr
     * - floor
     * - round
     * - ceiling
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
