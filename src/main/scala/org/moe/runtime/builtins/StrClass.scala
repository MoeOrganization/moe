package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Str
  */
object StrClass {

  def apply(r: MoeRuntime): Unit = {
    val strClass = r.getCoreClassFor("Str").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Str")
    )

  }

}
