package org.moe.runtime

class MoeBuiltins (private val runtime: MoeRuntime) {

  /**
   * setup class Class 
   */

  val classClass = runtime.getCoreClassFor("Class").getOrElse(throw new MoeErrors.MoeStartupError("Could not find class Class"))

  // constructor
  classClass.addMethod(
    new MoeMethod(
      "new",
      { (invocant, _) => invocant.asInstanceOf[MoeClass].newInstance }
    )
  )

  /**
   * setup class Int
   */

  val intClass = runtime.getCoreClassFor("Int").getOrElse(throw new MoeErrors.MoeStartupError("Could not find class Int"))

  // arithmetic
  
  intClass.addMethod(
    new MoeMethod(
      "+",
      { (lhs, args) =>
        val rhs = args(0)
        val i = lhs.asInstanceOf[MoeIntObject]
        rhs match {
          case rhs_i: MoeIntObject => runtime.NativeObjects.getInt(i.getNativeValue + rhs_i.getNativeValue)
          case rhs_n: MoeFloatObject => runtime.NativeObjects.getFloat(i.getNativeValue.toDouble + rhs_n.getNativeValue)
          case _ => throw new MoeErrors.UnexpectedType(rhs.toString)
        }
      }
    )
  )

  /**
   * setup class Num
   */

  val numClass = runtime.getCoreClassFor("Num").getOrElse(throw new MoeErrors.MoeStartupError("Could not find class Num"))

  numClass.addMethod(
    new MoeMethod(
      "+",
      { (lhs, args) =>
        val rhs = args(0)
        val n = lhs.asInstanceOf[MoeFloatObject]
        rhs match {
          case rhs_n: MoeFloatObject => runtime.NativeObjects.getFloat(n.getNativeValue + rhs_n.getNativeValue)
          case rhs_i: MoeIntObject => runtime.NativeObjects.getFloat(n.getNativeValue + rhs_i.getNativeValue.toDouble)
          case _ => throw new MoeErrors.UnexpectedType(rhs.toString)
        }
      }
    )
  )

}