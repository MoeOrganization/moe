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

    // Unary -

    intClass.addMethod(
      new MoeMethod(
        "prefix:<->",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].negate(r)
      )
    )

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
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].add(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].subtract(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].multiply(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].divide(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].modulo(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].pow(r, e.get("$other").get)
      )
    )

    // relational operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].less_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].greater_than(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    // equality operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].not_equal_to(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].compare_to(r, e.get("$other").get)
      )
    )

    // bitwise operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<&>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].bit_and(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<|>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].bit_or(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<^>",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].bit_xor(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<<>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].bit_shift_left(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].bit_shift_right(r, e.get("$other").get)
      )
    )

    // methods

    intClass.addMethod(
      new MoeMethod(
        "abs",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].abs(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "sin",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].sin(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "cos",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].cos(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "tan",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].tan(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "asin",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].asin(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "acos",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].acos(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "atan",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].atan(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "atan2",
        new MoeSignature(List(new MoeNamedParameter("$other"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].atan2(r, e.get("$other").get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "exp",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].exp(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "log",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].log(r)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "sqrt",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeIntObject].sqrt(r)
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
