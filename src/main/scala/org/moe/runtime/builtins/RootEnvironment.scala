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
    env.create("%ENV", getHash(r.getSystem.getEnv.map(p => p._1 -> getStr(p._2)).toSeq:_*))
    env.create("@INC", getArray(r.getIncludeDirs.map(getStr(_)):_*))
  }
}
