package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Array
  */
object ArrayClass {

  def apply(r: MoeRuntime): Unit = {
    val env        = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val arrayClass = r.getCoreClassFor("Array").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Array")
    )

    // MRO: Array, Any, Object

    /**
     * NOTE: 
     * postcircumfix:<[]> needs to also 
     * support things like ranges as well
     * as just simple indexing.
     * It should also support lvalue 
     * assignment as well.
     */

    // basic access
    arrayClass.addMethod(
      new MoeMethod(
        "postcircumfix:<[]>",
        new MoeSignature(List(new MoeParameter("$i"))),
        env,
        { (e) => 
            var index = e.get("$i").get.unboxToInt.get
            val array = e.getCurrentInvocant.get.unboxToArrayBuffer.get

            while (index < 0) index += array.size
            try {
              array(index)
            } catch {
              case _: java.lang.IndexOutOfBoundsException => r.NativeObjects.getUndef // TODO: warn
            }
        }
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "shift",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.unboxToArrayBuffer.get(0)
      )
    )

    /**
     * List of Methods to support:
     * - values 
     * - reverse
     * - at_pos ($index)
     * - bind_pos ($index, $value)
     * - slice (@indicies)
     * - clear
     * - exists ($value)
     * - delete ($index | @indicies) 
     * - push ($item | @items)
     * - pop
     * - unshift ($item | @items)
     * - sort ($sorter)
     * - grep ($filter)
     * - map ($callback)  << returns values
     * - each ($callback) << doesn't return values
     * - first ($predicate)
     * - join (?$seperator)
     * - min ($comparator)
     * - max ($comparator)
     * - sum
     * - length
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Array.pm
     */
  }

}
