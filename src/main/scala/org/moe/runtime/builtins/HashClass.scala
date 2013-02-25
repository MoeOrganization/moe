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

    // MRO: Hash, Any, Object

    import r.NativeObjects._

    /**
     * NOTE: 
     * This should also support slice and lvalue assignment.
     */
    hashClass.addMethod(
      new MoeMethod(
        "postcircumfix:<{}>",
        new MoeSignature(List(new MoeParameter("$key"))),
        env,
        { (e) => 
          val hash = e.getCurrentInvocant.get.asInstanceOf[MoeHashObject]
          hash.at_key(r, e.get("$key").get.asInstanceOf[MoeStrObject])
        }
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "at_key", 
        new MoeSignature(List(new MoeParameter("$key"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].at_key(r, e.get("$key").get.asInstanceOf[MoeStrObject])
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "bind_key", 
        new MoeSignature(List(new MoeParameter("$key"), new MoeParameter("$value"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].bind_key(r, e.get("$key").get.asInstanceOf[MoeStrObject], e.get("$value").get)
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "exists",
        new MoeSignature(List(new MoeParameter("$key"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].exists(r, e.get("$key").get.asInstanceOf[MoeStrObject])
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "slice",
        new MoeSignature(List(new MoeParameter("@keys"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].slice(r, e.get("@keys").get.asInstanceOf[MoeArrayObject])
      )
    )

    hashClass.addMethod(new MoeMethod("clear",  new MoeSignature(), env, (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].clear(r)))
    hashClass.addMethod(new MoeMethod("keys",   new MoeSignature(), env, (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].keys(r)))
    hashClass.addMethod(new MoeMethod("values", new MoeSignature(), env, (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].values(r)))
    hashClass.addMethod(new MoeMethod("kv",     new MoeSignature(), env, (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].kv(r)))
    hashClass.addMethod(new MoeMethod("pairs",  new MoeSignature(), env, (e) => e.getCurrentInvocant.get.asInstanceOf[MoeHashObject].pairs(r)))

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
