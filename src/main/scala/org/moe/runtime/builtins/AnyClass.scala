package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Any
  */
object AnyClass {

  def apply(r: MoeRuntime): Unit = {
    val anyClass = r.getCoreClassFor("Any").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Any")
    )

  }

}
