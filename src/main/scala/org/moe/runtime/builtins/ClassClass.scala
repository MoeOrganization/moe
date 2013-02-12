package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Class 
  */
object ClassClass {

  def apply(r: MoeRuntime): Unit = {
    val classClass = r.getCoreClassFor("Class").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Class")
    )

    // constructor
    classClass.addMethod(
      new MoeMethod(
        "new",
        { (invocant, _) => invocant.asInstanceOf[MoeClass].newInstance }
      )
    )  
  }

}
