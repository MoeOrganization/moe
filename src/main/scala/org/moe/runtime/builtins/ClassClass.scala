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

    classClass.addMethod(
      new MoeMethod(
        "name",
        new MoeSignature(),
        env,
        (e) => getStr(self(e).getName)
      )
    )

    classClass.addMethod(
      new MoeMethod(
        "version",
        new MoeSignature(),
        env,
        (e) => self(e).getVersion match {
          case Some(v) => getStr(v)
          case None    => getUndef
        }
      )
    )

    classClass.addMethod(
      new MoeMethod(
        "authority",
        new MoeSignature(),
        env,
        (e) => self(e).getAuthority match {
          case Some(a) => getStr(a)
          case None    => getUndef
        }
      )
    )

    classClass.addMethod(
      new MoeMethod(
        "superclass",
        new MoeSignature(),
        env,
        (e) => self(e).getSuperclass.getOrElse(getUndef)
      )
    )

    /**
     * List of Methods to support:
     * 
     *
     */
  }

}
