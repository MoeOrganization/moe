package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Object
  */
object ObjectClass {

  def apply(r: MoeRuntime): Unit = {
    val env         = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val objectClass = r.getCoreClassFor("Object").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Object")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeClass = e.getCurrentInvocantAs[MoeClass].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )


    // MRO: Any, Object

    objectClass.addMethod(
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
     * See the following for details:
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/.pm
     */


  }

}
