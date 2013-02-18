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

  }

}
