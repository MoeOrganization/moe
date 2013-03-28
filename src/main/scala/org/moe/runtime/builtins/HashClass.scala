package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Hash
  */
object HashClass {

  def apply(r: MoeRuntime): Unit = {
    val env       = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val hashClass = r.getCoreClassFor("Hash").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Hash")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeHashObject = e.getCurrentInvocantAs[MoeHashObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Hash, Any, Object

    import r.NativeObjects._

    /**
     * NOTE: 
     * This should also support slice and lvalue assignment.
     */
    hashClass.addMethod(
      new MoeMethod(
        "postcircumfix:<{}>",
        new MoeSignature(List(new MoeNamedParameter("$key"))),
        env,
        { (e) => 
          val hash = self(e)
          hash.at_key(r, e.getAs[MoeStrObject]("$key").get)
        }
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "at_key", 
        new MoeSignature(List(new MoeNamedParameter("$key"))),
        env,
        (e) => self(e).at_key(r, e.getAs[MoeStrObject]("$key").get)
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "bind_key", 
        new MoeSignature(List(new MoeNamedParameter("$key"), new MoeNamedParameter("$value"))),
        env,
        (e) => self(e).bind_key(r, e.getAs[MoeStrObject]("$key").get, e.get("$value").get)
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "exists",
        new MoeSignature(List(new MoeNamedParameter("$key"))),
        env,
        (e) => self(e).exists(r, e.getAs[MoeStrObject]("$key").get)
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "slice",
        new MoeSignature(List(new MoeSlurpyParameter("@keys"))),
        env,
        (e) => self(e).slice(r, e.getAs[MoeArrayObject]("@keys").get)
      )
    )

    hashClass.addMethod(new MoeMethod("clear",  new MoeSignature(), env, (e) => self(e).clear(r)))
    hashClass.addMethod(new MoeMethod("keys",   new MoeSignature(), env, (e) => self(e).keys(r)))
    hashClass.addMethod(new MoeMethod("values", new MoeSignature(), env, (e) => self(e).values(r)))
    hashClass.addMethod(new MoeMethod("kv",     new MoeSignature(), env, (e) => self(e).kv(r)))
    hashClass.addMethod(new MoeMethod("pairs",  new MoeSignature(), env, (e) => self(e).pairs(r)))

    /**
     * List of Methods to support:
     * - each ($callback)
     * - delete ($key | @keys)
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Hash.pm
     */
  }

}
