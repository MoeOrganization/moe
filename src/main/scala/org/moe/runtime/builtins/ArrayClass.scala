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

    import r.NativeObjects.Unbox._

    // basic access
    arrayClass.addMethod(
      new MoeMethod(
        "postcircumfix:<[]>",
        { (invocant, args) => 

            var index = toInt(args(0)).get
            val array = toArray(invocant).get

            while (index < 0) {
              index += array.size
            }

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
