package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class IO
  */
object IOClass {

  def apply(r: MoeRuntime): Unit = {
    val env     = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val ioClass = r.getCoreClassFor("IO").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class IO")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeIOObject = e.getCurrentInvocantAs[MoeIOObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: IO, Any, Object

    ioClass.addMethod(
      new MoeMethod(
        "new",
        new MoeSignature(List(new MoePositionalParameter("$path"))),
        env,
        (e) => new MoeIOObject(
          new java.io.File(e.getAs[MoeStrObject]("$path").get.unboxToString.get),
          Some(ioClass)
        )
      )
    )

    // file test operators

    ioClass.addMethod(
      new MoeMethod(
        "prefix:<-e>",
        new MoeSignature(),
        env,
        (e) => self(e).exists(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "prefix:<-r>",
        new MoeSignature(),
        env,
        (e) => self(e).isReadable(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "prefix:<-w>",
        new MoeSignature(),
        env,
        (e) => self(e).isWriteable(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "prefix:<-x>",
        new MoeSignature(),
        env,
        (e) => self(e).isExecutable(r)
      )
    )

    // methods

    ioClass.addMethod(
      new MoeMethod(
        "touch",
        new MoeSignature(),
        env,
        (e) => self(e).create(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "unlink",
        new MoeSignature(),
        env,
        (e) => self(e).delete(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "print",
        new MoeSignature(List(new MoeSlurpyParameter("@data"))),
        env,
        (e) => self(e).print(r, e.getAs[MoeArrayObject]("@data").get)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "say",
        new MoeSignature(List(new MoeSlurpyParameter("@data"))),
        env,
        (e) => self(e).say(r, e.getAs[MoeArrayObject]("@data").get)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "readline",
        new MoeSignature(),
        env,
        (e) => self(e).readline(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "readlines",
        new MoeSignature(),
        env,
        (e) => self(e).readlines(r)
      )
    )

    ioClass.addMethod(
      new MoeMethod(
        "slurp",
        new MoeSignature(),
        env,
        (e) => self(e).slurp(r)
      )
    )

    /**
     * NOTE:
     * This list needs some work, I have been punting on
     * IO for a while now, so have not been giving it to 
     * much thought.
     *
     * List of Operators to support:
     * - infix:<-d>
     * - infix:<-f>
     * - infix:<-l>
     * - infix:<-s>
     * - infix:<-z>
     *
     * List of Methods to support:
     * - printf ($format, @strings)
     * - read ($length)
     * - readline
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
     * - http://docs.oracle.com/javase/6/docs/api/java/io/File.html
     */
  }
}
