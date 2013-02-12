package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Undef
  */
object UndefClass {

  def apply(r: MoeRuntime): Unit = {
    val undefClass = r.getCoreClassFor("Undef").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Undef")
    )

  }

}
