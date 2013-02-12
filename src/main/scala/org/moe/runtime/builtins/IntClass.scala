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

    // arithmetic
    
    intClass.addMethod(
      new MoeMethod(
        "+",
        { (lhs, args) =>
          val rhs = args(0)
          val i = lhs.asInstanceOf[MoeIntObject]
          rhs match {
            case rhs_i: MoeIntObject => r.NativeObjects.getInt(i.getNativeValue + rhs_i.getNativeValue)
            case rhs_n: MoeFloatObject => r.NativeObjects.getFloat(i.getNativeValue.toDouble + rhs_n.getNativeValue)
            case _ => throw new MoeErrors.UnexpectedType(rhs.toString)
          }
        }
      )
    )
  }

}
