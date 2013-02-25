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

    // arithmetic
    
    intClass.addMethod(
      new MoeMethod(
        "infix:<+>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getInt(e.getCurrentInvocant.get.unboxToInt.get + i.unboxToInt.get)
          case f: MoeNumObject => getNum(e.getCurrentInvocant.get.unboxToDouble.get + f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<->",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getInt(e.getCurrentInvocant.get.unboxToInt.get - i.unboxToInt.get)
          case f: MoeNumObject => getNum(e.getCurrentInvocant.get.unboxToDouble.get - f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<*>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getInt(e.getCurrentInvocant.get.unboxToInt.get * i.unboxToInt.get)
          case f: MoeNumObject => getNum(e.getCurrentInvocant.get.unboxToDouble.get * f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:</>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => getNum(e.getCurrentInvocant.get.unboxToInt.get / e.get("$other").get.unboxToDouble.get)
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<%>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => getInt(perlModuloOp(e.getCurrentInvocant.get.unboxToInt.get, e.get("$other").get.unboxToInt.get))
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<**>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getInt(Math.pow(e.getCurrentInvocant.get.unboxToInt.get, i.unboxToInt.get).toInt)
          case f: MoeNumObject => getNum(Math.pow(e.getCurrentInvocant.get.unboxToInt.get, f.unboxToDouble.get))
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    // relational operators

    intClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get < i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get < f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get < i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get < f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get > i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get > f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<<=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get <= i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get <= f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<>=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get >= i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get >= f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<==>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get == i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get == f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
      )
    )

    intClass.addMethod(
      new MoeMethod(
        "infix:<!=>",
        new MoeSignature(List(new MoeParameter("$other"))), 
        env, 
        (e) => e.get("$other").get match {
          case i: MoeIntObject => getBool(e.getCurrentInvocant.get.unboxToInt.get != i.unboxToInt.get)
          case f: MoeNumObject => getBool(e.getCurrentInvocant.get.unboxToDouble.get != f.unboxToDouble.get)
          case _               => throw new MoeErrors.UnexpectedType(e.get("$other").get.toString)
        }
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
