package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Hash
  */
object HashClass {

  def apply(r: MoeRuntime): Unit = {
    val hashClass = r.getCoreClassFor("Hash").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Hash")
    )

    // MRO: Hash, Any, Object

    import r.NativeObjects._

    /**
     * NOTE: 
     * postcircumfix:<{}> needs to also 
     * support multiple keys, not just 
     * as single key indexing.
     * It should also support lvalue 
     * assignment as well.
     * It should delegate to the methods
     * below.
     */
    hashClass.addMethod(
      new MoeMethod(
        "postcircumfix:<{}>",
        { (self, args) => 
          val key  = args(0).unboxToString.get
          val hash = self.unboxToMap.get
          hash.get(key).getOrElse(r.NativeObjects.getUndef)
        }
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "at_key", // ($key)
        (self, args) => self.asInstanceOf[MoeHashObject].at_key(args(0).asInstanceOf[MoeStrObject])
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "bind_key", // ($key, $value)
        (self, args) => self.asInstanceOf[MoeHashObject].bind_key(args(0).asInstanceOf[MoeStrObject], args(1))
      )
    )

    hashClass.addMethod(new MoeMethod("keys", (self, _) => self.asInstanceOf[MoeHashObject].keys))

    hashClass.addMethod(new MoeMethod("values", (self, _) => self.asInstanceOf[MoeHashObject].values))

    hashClass.addMethod(new MoeMethod("kv", (self, _) => self.asInstanceOf[MoeHashObject].kv))

    hashClass.addMethod(new MoeMethod("pairs", (self, _) => self.asInstanceOf[MoeHashObject].pairs))

    /**
     * List of Methods to support:
     * - each ($callback)
     * - delete ($key | @keys)
     * - exists ($key)
     * - slice (@keys)
     * - clear     
     * - delete ($key | @keys) 
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Hash.pm
     */
  }

}
