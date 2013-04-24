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
        new MoeSignature(List(new MoePositionalParameter("$i"), new MoeOptionalParameter("$value"))),
        env,
        { (e) => 
            var index = e.get("$i").get.unboxToInt.get
            val array = self(e).unboxToArrayBuffer.get

            while (index < 0) index += array.size

            try {
              e.get("$value") match {
                case Some(none:  MoeUndefObject) => array(index)
                case Some(value: MoeObject)      => self(e).bind_pos(r, getInt(index), value)
                case _                           => array(index)
              }
            } catch {
              case _: java.lang.IndexOutOfBoundsException => getUndef // TODO: warn
            }
        }
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "at_pos",
        new MoeSignature(List(new MoePositionalParameter("$i"))),
        env,
        (e) => self(e).at_pos(r, e.getAs[MoeIntObject]("$i").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "bind_pos",
        new MoeSignature(List(new MoePositionalParameter("$i"), new MoePositionalParameter("$v"))),
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
        "head",
        new MoeSignature(),
        env,
        (e) => self(e).head(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "tail",
        new MoeSignature(),
        env,
        (e) => self(e).tail(r)
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
        (e) => e.get("$sep").get match {
            case (sep: MoeStrObject) => self(e).join(r, sep)
            case _                   => self(e).join(r)
        }
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "map",
        new MoeSignature(List(new MoePositionalParameter("&f"))),
        env,
        (e) => self(e).map(r, e.getAs[MoeCode]("&f").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "grep",
        new MoeSignature(List(new MoePositionalParameter("&f"))),
        env,
        (e) => self(e).grep(r, e.getAs[MoeCode]("&f").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "each",
        new MoeSignature(List(new MoePositionalParameter("&f"))),
        env,
        (e) => self(e).each(r, e.getAs[MoeCode]("&f").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "reduce",
        new MoeSignature(List(new MoePositionalParameter("&f"), new MoeOptionalParameter("$init"))),
        env,
        { (e) => e.get("$init") match {
            case Some(none: MoeUndefObject) => self(e).reduce(r, e.getAs[MoeCode]("&f").get, None)
            case Some(init: MoeObject) => self(e).reduce(r, e.getAs[MoeCode]("&f").get, Some(init))
            case _                     => self(e).reduce(r, e.getAs[MoeCode]("&f").get, None)
          }
        }
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "first",
        new MoeSignature(List(new MoePositionalParameter("&f"))),
        env,
        (e) => self(e).first(r, e.getAs[MoeCode]("&f").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "max",
        new MoeSignature(),
        env,
        (e) => self(e).max(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "maxstr",
        new MoeSignature(),
        env,
        (e) => self(e).maxstr(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "min",
        new MoeSignature(),
        env,
        (e) => self(e).min(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "minstr",
        new MoeSignature(),
        env,
        (e) => self(e).minstr(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "shuffle",
        new MoeSignature(),
        env,
        (e) => self(e).shuffle(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "sum",
        new MoeSignature(),
        env,
        (e) => self(e).sum(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "eqv",
        new MoeSignature(List(new MoePositionalParameter("@that"))),
        env,
        (e) => self(e).equal_to(r, e.getAs[MoeArrayObject]("@that").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "flatten",
        new MoeSignature(),
        env,
        (e) => self(e).flatten(r)
      )
    )

    // repetition

    arrayClass.addMethod(
      new MoeMethod(
        "infix:<x>",
        new MoeSignature(List(new MoePositionalParameter("$times"))),
        env,
        (e) => self(e).repeat(r, e.getAs[MoeIntObject]("$times").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "exists",
        new MoeSignature(List(new MoePositionalParameter("$item"))),
        env,
        (e) => self(e).exists(r, e.get("$item").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "uniq",
        new MoeSignature(),
        env,
        (e) => self(e).uniq(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "zip",
        new MoeSignature(List(new MoePositionalParameter("@that"))),
        env,
        (e) => self(e).zip(r, e.getAs[MoeArrayObject]("@that").get)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "kv",
        new MoeSignature(),
        env,
        (e) => self(e).kv(r)
      )
    )

    arrayClass.addMethod(
      new MoeMethod(
        "classify",
        new MoeSignature(List(new MoePositionalParameter("&mapper"))),
        env,
        (e) => self(e).classify(r, e.getAs[MoeCode]("&mapper").get)
      )
    )

    /**
     * List of Methods to support:
     * - delete ($index | @indicies) 
     * - sort ($sorter)
     * - min ($comparator)
     * - max ($comparator)
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Array.pm
     */
  }

}
