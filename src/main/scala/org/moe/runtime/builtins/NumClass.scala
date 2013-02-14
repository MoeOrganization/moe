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

    import r.NativeObjects._

    numClass.addMethod(
      new MoeMethod(
        "+",
        { (lhs, args) =>
          val rhs = args(0)
          val n = lhs.asInstanceOf[MoeFloatObject]
          rhs match {
            case rhs_n: MoeFloatObject => getFloat(n.getNativeValue + rhs_n.getNativeValue)
            case rhs_i: MoeIntObject   => getFloat(n.getNativeValue + rhs_i.getNativeValue.toDouble)
            case _                     => throw new MoeErrors.UnexpectedType(rhs.toString)
          }
        }
      )
    )
  }

}
