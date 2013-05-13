package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup root environment
  */
object RootEnvironment {

  def apply(r: MoeRuntime): Unit = {
    val env = r.getRootEnv   

    import r.NativeObjects._

    env.create("@ARGV", getArray())

    // SEE ALSO -> http://perlcabal.org/syn/S28.html

    env.create("$!", getUndef)

    env.create("%ENV", getHash(r.getEnv))

    env.create("@INC", getArray(r.getIncludeDirs))
    env.create("%INC",  getHash())
  }
}
