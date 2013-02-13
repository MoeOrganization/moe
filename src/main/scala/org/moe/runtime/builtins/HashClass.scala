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


    // basic access
    hashClass.addMethod(
      new MoeMethod(
        "postcircumfix:<{}>",
        { (invocant, args) => 

            var key  = objToString(args(0))
            val hash = invocant match {
              case h: MoeHashObject => h.getNativeValue
              case _                => throw new MoeErrors.UnexpectedType("MoeHashObject expected")
            }

            hash.get(key).getOrElse(r.NativeObjects.getUndef)
        }
     )
    )
  }

}
