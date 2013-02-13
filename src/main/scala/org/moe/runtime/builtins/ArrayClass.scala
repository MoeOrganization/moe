package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.interpreter.InterpreterUtils._

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

            var index = objToInteger(args(0))
            val array = invocant match {
              case a: MoeArrayObject => a.getNativeValue
              case _                 => throw new MoeErrors.UnexpectedType("MoeArrayObject expected")
            }

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
