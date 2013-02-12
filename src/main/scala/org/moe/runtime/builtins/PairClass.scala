package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Pair
  */
object PairClass {

  def apply(r: MoeRuntime): Unit = {
    val pairClass = r.getCoreClassFor("Pair").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Pair")
    )

  }

}
