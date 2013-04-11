package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

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

    def klass(e: MoeEnvironment): MoeClass = e.getCurrentInvocantAs[MoeClass].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    def self(e: MoeEnvironment): MoeObject = e.getCurrentInvocantAs[MoeObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Any, Object

    objectClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(List(new MoeSlurpyNamedParameter("%args"))),
        env,
        {
          (e) => 
            val cls  = klass(e)
            val inv  = cls.newInstance.asInstanceOf[MoeOpaque]
            val args = e.get("%args").get match {
              case (x: MoeHashObject)  => x
              case (x: MoeUndefObject) => getHash()
            }
            cls.collectAllAttributes.foreach(
              { a => 
                  val attr = a._2
                  val key  = getStr(attr.getKeyName)
                  inv.setValue(
                    attr.getName,
                    if (args.exists(r, key).isTrue) args.at_key(r, key) else attr.getDefault.getOrElse(getUndef)
                  )
              }
            )
            inv
        }
      )
    )

    objectClass.addMethod(
      new MoeMethod(
        "class",
        new MoeSignature(),
        env,
        (e) => self(e).getAssociatedClass.get
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
