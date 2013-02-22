package org.moe.runtime.builtins

import org.moe.runtime._

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
        { (invocant, args) => 
          val key  = args(0).unboxToString.get
          val hash = invocant.unboxToMap.get
          hash.get(key).getOrElse(r.NativeObjects.getUndef)
        }
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "at_key", // ($key)
        { (invocant, args) => 
          val key  = args(0).unboxToString.get
          val hash = invocant.unboxToMap.get
          hash.get(key).getOrElse(r.NativeObjects.getUndef)
        }
      )
    )

    hashClass.addMethod(
      new MoeMethod(
        "bind_key", // ($key, $value)
        { (invocant, args) => 
          val key   = args(0).unboxToString.get
          val value = args(1)
          val hash  = invocant.unboxToMap.get
          hash.put(key, value)
          value
        }
      )
    )

    hashClass.addMethod(new MoeMethod("keys", { (invocant, _) => 
      getArray(invocant.unboxToMap.get.keys.map(getString(_)).toList)
    }))

    hashClass.addMethod(new MoeMethod("values",{ (invocant, _) => 
      getArray(invocant.unboxToMap.get.values.map(x => x).toList)
    }))

    hashClass.addMethod(new MoeMethod("kv",{ (invocant, _) => 
      getArray(invocant.unboxToMap.get.toList.map(
        p => getArray(List(getString(p._1), p._2))
      ))
    }))

    hashClass.addMethod(new MoeMethod("pairs",{ (invocant, _) => 
      getArray(invocant.unboxToMap.get.toList.map(
        p => getPair(getString(p._1) -> p._2)
      ))
    }))

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
