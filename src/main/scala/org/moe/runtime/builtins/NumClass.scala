package org.moe.runtime.builtins

import org.moe.runtime._
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

    numClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getNum(e.getCurrentInvocant.get.unboxToDouble.get + e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getNum(e.getCurrentInvocant.get.unboxToDouble.get - e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getNum(e.getCurrentInvocant.get.unboxToDouble.get * e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getNum(e.getCurrentInvocant.get.unboxToDouble.get / e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getInt(perlModuloOp(e.getCurrentInvocant.get.unboxToInt.get, e.get("$other").get.unboxToInt.get))
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getNum(Math.pow(e.getCurrentInvocant.get.unboxToDouble.get, e.get("$other").get.unboxToDouble.get))
      )
    )

    // relational operators

    numClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get < e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get > e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get <= e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get >= e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get == e.get("$other").get.unboxToDouble.get)
      )
    )

    numClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoeParameter("$other"))),
        env,
        (e) => getBool(e.getCurrentInvocant.get.unboxToDouble.get != e.get("$other").get.unboxToDouble.get)
      )
    )

    /**
     * List of Operators to support:
     * - prefix:<++>
     * - postfix:<++>
     * - prefix:<-->
     * - postfix:<-->
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
