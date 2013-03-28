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

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeArrayObject = e.getCurrentInvocantAs[MoeArrayObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
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
            val array = self(e).unboxToArrayBuffer.get

            while (index < 0) index += array.size
            try {
              array(index)
            } catch {
              case _: java.lang.IndexOutOfBoundsException => getUndef // TODO: warn
            }
        }
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "at_pos",
        new MoeSignature(List(new MoeNamedParameter("$i"))),
        env,
        (e) => self(e).at_pos(r, e.getAs[MoeIntObject]("$i").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "bind_pos",
        new MoeSignature(List(new MoeNamedParameter("$i"), new MoeNamedParameter("$v"))),
        env,
        (e) => self(e).bind_pos(
            r, 
            e.getAs[MoeIntObject]("$i").get, 
            e.get("$v").get
        )
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "length",
        new MoeSignature(),
        env,
        (e) => self(e).length(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "clear",
        new MoeSignature(),
        env,
        (e) => self(e).clear(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "shift",
        new MoeSignature(),
        env,
        (e) => self(e).shift(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "pop",
        new MoeSignature(),
        env,
        (e) => self(e).pop(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "unshift",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => self(e).unshift(r, e.getAs[MoeArrayObject]("@items").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "push",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => self(e).push(r, e.getAs[MoeArrayObject]("@items").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "slice",
        new MoeSignature(List(new MoeSlurpyParameter("@items"))),
        env,
        (e) => self(e).slice(r, e.getAs[MoeArrayObject]("@items").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "reverse",
        new MoeSignature(),
        env,
        (e) => self(e).reverse(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "join",
        new MoeSignature(List(new MoeOptionalParameter("$sep"))),
        env,
        (e) => e.getAs[MoeStrObject]("$sep") match {
            case Some(sep) => self(e).join(r, sep)
            case None      => self(e).join(r)
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
