package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class CoreClass 
  */
object CoreClassClass {

  def apply(r: MoeRuntime): Unit = {
    val env            = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val coreClassClass = r.getCoreClassFor("CoreClass").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class CoreClass")
    )

    import r.NativeObjects._

    def klass(e: MoeEnvironment): MoeClass = e.getCurrentInvocantAs[MoeClass].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: CoreClass Class, Object

    coreClassClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        {
          (e) => 
            val cls = klass(e)
            cls.getSubMethod("CREATE").getOrElse(
              throw new MoeErrors.NotAllowed("Cannot call constructor for " + cls.getName)
            ).execute(
              new MoeArguments(
                e.get("@_").get.unboxToArrayBuffer.get.toList,
                Some(cls)
              )
            )
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
