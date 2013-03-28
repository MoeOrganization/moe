package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

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
        new MoeSignature(List(new MoeNamedParameter("$i"))),
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
        "at_pos",
        new MoeSignature(List(new MoeNamedParameter("$i"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].at_pos(r, e.get("$i").get.asInstanceOf[MoeIntObject])
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "bind_pos",
        new MoeSignature(List(new MoeNamedParameter("$i"), new MoeNamedParameter("$v"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].bind_pos(
            r, 
            e.get("$i").get.asInstanceOf[MoeIntObject], 
            e.get("$v").get
        )
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "length",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].length(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "clear",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].clear(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "shift",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].shift(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "pop",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].pop(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "unshift",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].unshift(r, e.get("@items").get.asInstanceOf[MoeArrayObject])
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "push",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].push(r, e.get("@items").get.asInstanceOf[MoeArrayObject])
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "slice",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].slice(r, e.get("@items").get.asInstanceOf[MoeArrayObject])
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "reverse",
        new MoeSignature(),
        env,
        (e) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].reverse(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "join",
        new MoeSignature(List(new MoeOptionalParameter("$sep"))),
        env,
        (e) => e.get("$sep") match {
            case Some(sep) => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].join(r, sep.asInstanceOf[MoeStrObject])
            case None      => e.getCurrentInvocant.get.asInstanceOf[MoeArrayObject].join(r)
        }
      )
    )

    /**
     * List of Methods to support:
     * - exists ($value)
     * - delete ($index | @indicies) 
     * - sort ($sorter)
     * - grep ($filter)
     * - map ($callback)  << returns values
     * - each ($callback) << doesn't return values
     * - first ($predicate)
     * - min ($comparator)
     * - max ($comparator)
     * - sum
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Array.pm
     */
  }

}
