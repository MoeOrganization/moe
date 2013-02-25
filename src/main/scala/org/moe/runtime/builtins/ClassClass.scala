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

    // MRO: Class, Object

    // constructor
    classClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(List()),
        env,
        { (e) => e.getCurrentInvocant.get.asInstanceOf[MoeClass].newInstance }
      )
    )  

    /**
     * List of Methods to support:
     * 
     *
     */
  }

}
