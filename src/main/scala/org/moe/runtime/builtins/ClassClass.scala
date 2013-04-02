package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Class 
  */
object ClassClass {

  def apply(r: MoeRuntime): Unit = {
    val env        = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val classClass = r.getCoreClassFor("Class").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Class")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeClass = e.getCurrentInvocantAs[MoeClass].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Class, Object  

    /**
     * List of Methods to support:
     * 
     *
     */
  }

}
