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

    import r.NativeObjects._

    def klass(e: MoeEnvironment): MoeClass = e.getCurrentInvocantAs[MoeClass].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    def self(e: MoeEnvironment): MoeOpaque = e.getCurrentInvocantAs[MoeOpaque].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Exception, Scalar, Any, Object

    exceptionClass.addAttribute(new MoeAttribute("$!msg", Some(() => getUndef)))

    exceptionClass.addSubMethod(
      new MoeMethod(
        "CREATE",
        new MoeSignature(List(new MoeOptionalParameter("$msg"))),
        env,
        { (e) => 
            val cls  = klass(e)
            val ctor = cls.getMethod("new").get
            e.get("$msg") match {
              case Some(m) => cls.callMethod(ctor, List(getPair("msg", m)))
              case _       => cls.callMethod(ctor)
            }
        }
      )
    )

    exceptionClass.addMethod(
      new MoeMethod(
        "msg",
        new MoeSignature(),
        env,
        (e) => self(e).getValue("$!msg").get
      )
    )

    exceptionClass.addMethod(
      new MoeMethod(
        "throw",
        new MoeSignature(),
        env,
        (e) => throw new MoeErrors.MoeProblems(self(e).getValue("$!msg").get.unboxToString.get)
      )
    )

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
