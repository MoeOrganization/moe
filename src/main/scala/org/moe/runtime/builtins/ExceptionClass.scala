package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Exception
  */
object ExceptionClass {

  def apply(r: MoeRuntime): Unit = {
    val exceptionClass = r.getCoreClassFor("Exception").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Exception")
    )

  }

}
