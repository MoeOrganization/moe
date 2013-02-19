package org.moe.runtime.builtins

import org.moe.runtime._

/**
  * setup class IO
  */
object IOClass {

  def apply(r: MoeRuntime): Unit = {
    val ioClass = r.getCoreClassFor("IO").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class IO")
    )

    // MRO: IO, Any, Object

    /**
     * NOTE:
     * This list needs some work, I have been punting on
     * IO for a while now, so have not been giving it to 
     * much thought.
     *
     * List of Operators to support:
     * - infix:<-d>
     * - infix:<-e>
     * - infix:<-f>
     * - infix:<-l>
     * - infix:<-r>
     * - infix:<-s>
     * - infix:<-w>
     * - infix:<-x>
     * - infix:<-z>
     *
     * List of Methods to support:
     * - say (@strings)
     * - print (@strings)
     * - printf ($format, @strings)
     * - read ($length)
     * - readline
     * - write (@strings)
     * - open ($mode)
     * - is_open
     * - close
     * - eof
     * - getc
     * - putc ($c)
     * - seek ($whence, $offset)
     * - slurp
     * - flush
     *
     * See the following for details:
     * - https://metacpan.org/module/IO::Handle << with a grain of salt
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/IO.pm
     */
  }
}
