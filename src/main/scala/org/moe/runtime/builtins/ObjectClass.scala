package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Object
  */
object ObjectClass {

  def apply(r: MoeRuntime): Unit = {
    val objectClass = r.getCoreClassFor("Object").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Object")
    )

  }

}
