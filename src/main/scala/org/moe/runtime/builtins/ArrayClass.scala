package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Array
  */
object ArrayClass {

  def apply(r: MoeRuntime): Unit = {
    val arrayClass = r.getCoreClassFor("Array").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Array")
    )

    // basic access
    arrayClass.addMethod(
      new MoeMethod(
        "postcircumfix:<[]>",
        { (invocant, args) => 

            var index = args(0).unboxToInt.get
            val array = invocant.unboxToArray.get

            while (index < 0) index += array.size
            try {
              array(index)
            } catch {
              case _: java.lang.IndexOutOfBoundsException => r.NativeObjects.getUndef // TODO: warn
            }
        }
      )
    )
  }

}
