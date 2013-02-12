package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Scalar
  */
object ScalarClass {

  def apply(r: MoeRuntime): Unit = {
    val scalarClass = r.getCoreClassFor("Scalar").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Scalar")
    )

  }

}
