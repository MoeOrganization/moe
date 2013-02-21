package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.interpreter.InterpreterUtils._

/**
  * setup class Hash
  */
object HashClass {

  def apply(r: MoeRuntime): Unit = {
    val hashClass = r.getCoreClassFor("Hash").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Hash")
    )

    // MRO: Hash, Any, Object

    /**
     * NOTE: 
     * postcircumfix:<{}> needs to also 
     * support multiple keys, not just 
     * as single key indexing.
     * It should also support lvalue 
     * assignment as well.
     */

    // basic access
    hashClass.addMethod(
      new MoeMethod(
        "postcircumfix:<{}>",
        { (invocant, args) => 

            var key  = args(0).unboxToString.get
            val hash = invocant.unboxToMap.get

            hash.get(key).getOrElse(r.NativeObjects.getUndef)
        }
     )
    )

    /**
     * List of Methods to support:
     * - keys 
     * - values
     * - kv (returns an AoA of the pairs)
     * - pairs (returns an array of pairs)
     * - each ($callback)
     * - delete ($key | @keys)
     * - exists ($key)
     * - at_key ($key)
     * - bind_key ($key, $value)
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
