package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Bool
  */
object BoolClass {

  def apply(r: MoeRuntime): Unit = {
    val boolClass = r.getCoreClassFor("Bool").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Bool")
    )

  }

}
