package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Num 
  */
object NumClass {

  def apply(r: MoeRuntime): Unit = {
    val numClass = r.getCoreClassFor("Num").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Num")
    )

    // MRO: Num, Scalar, Any, Object

    import r.NativeObjects._

    numClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        { (invocant, args) =>
          getNum(invocant.unboxToDouble.get + args(0).unboxToDouble.get)
        }
      )
    )

    /**
     * List of Operators to support:
     * - infix:<*> 
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
