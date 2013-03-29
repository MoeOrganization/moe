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

    // constructor
    classClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(),
        env,
        {
          (e) => 
            val c = self(e)
            val i = c.newInstance.asInstanceOf[MoeOpaque]
            c.collectAllAttributes.foreach(a => a._2.getDefault.map(i.setValue(a._1, _)))
            i
        }
      )
    )  

    /**
     * List of Methods to support:
     * 
     *
     */
  }

}
