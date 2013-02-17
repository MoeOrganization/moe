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
        { (invocant, args) =>
          args(0) match {
            case i: MoeIntObject   => getFloat(invocant.unboxToDouble.get + i.unboxToDouble.get)
            case f: MoeFloatObject => getFloat(invocant.unboxToDouble.get + f.unboxToDouble.get)
            case _                 => throw new MoeErrors.UnexpectedType(invocant.toString)
          }
        }
      )
    )
  }

}
