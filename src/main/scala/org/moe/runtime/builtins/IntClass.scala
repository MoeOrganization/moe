package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Int 
  */
object IntClass {

  def apply(r: MoeRuntime): Unit = {
    val intClass = r.getCoreClassFor("Int").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Int")
    )

    // MRO: Int, Scalar, Any, Object

    import r.NativeObjects._

    // arithmetic
    
    intClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        { (invocant, args) =>
          args(0) match {
            case i: MoeIntObject   => getInt(invocant.unboxToInt.get + i.unboxToInt.get)
            case f: MoeFloatObject => getFloat(invocant.unboxToDouble.get + f.unboxToDouble.get)
            case _                 => throw new MoeErrors.UnexpectedType(args(0).toString)
          }
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        { (invocant, args) =>
          args(0) match {
            case i: MoeIntObject   => getInt(invocant.unboxToInt.get * i.unboxToInt.get)
            case f: MoeFloatObject => getFloat(invocant.unboxToDouble.get * f.unboxToDouble.get)
            case _                 => throw new MoeErrors.UnexpectedType(args(0).toString)
          }
        }
      )
    )

    /**
     * List of Operators to support:
     * - infix:<-> 
     * - infix:</>
     * - infix:<%>
     * - prefix:<++>
     * - postfix:<++>
     * - prefix:<-->
     * - postfix:<-->
     * - prefix:<->
     * - infix:<**>
     * - infix:<==>
     * - infix:<!=>
     * - infix:<<>
     * - infix:<>>
     * - infix:<<=>
     * - infix:<>=>
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
