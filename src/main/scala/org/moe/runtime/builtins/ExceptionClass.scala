package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Exception
  */
object ExceptionClass {

  def apply(r: MoeRuntime): Unit = {
    val env            = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val exceptionClass = r.getCoreClassFor("Exception").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Exception")
    )

    // MRO: Exception, Scalar, Any, Object

    /**
     * List of Methods to support:
     * - throw (?$msg)
     * 
     *
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Exception.pm
     */

  }

}
