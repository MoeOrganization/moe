package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class Str
  */
object StrClass {

  def apply(r: MoeRuntime): Unit = {
    val strClass = r.getCoreClassFor("Str").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Str")
    )

    // MRO: Str, Scalar, Any, Object

    /**
     * List of Operators to support:
     * - infix:<.> 
     *
     * List of Methods to support:
     * - chomp
     * - chop
     * - lc
     * - lcfirst
     * - uc
     * - ucfirst
     * - length
     * - reverse
     * - index ($substring, ?$position)
     * - rindex ($substring, ?$position)
     * - sprintf ($format, @items)
     * - substr ($offset, ?$length)
     * - split ($string)
     * - concat ($string | @strings)
     * - quotemeta
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Str.pm
     */

  }

}
