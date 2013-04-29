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
    env.create("%INC",  getHash())

    // SEE ALSO -> http://perlcabal.org/syn/S28.html

    env.create("$OS_ERROR", getUndef)
    env.create("$!", getUndef)

    /**
     * TODO:
     * we need to be able to alter 
     * these at runtime and they 
     * then alter their sources, 
     * the ENV for any child processes
     * and the INC to load in modules
     * this means we will have to 
     * implement some kind of tied
     * data structure.
     * For right now they are basically
     * just snapshots of these variables
     * and aren't reflecting correctly
     * in the runtime.
     */ 
    env.create("%ENV", getHash(r.getEnv))
    env.create("@INC", getArray(r.getIncludeDirs))
  }
}
