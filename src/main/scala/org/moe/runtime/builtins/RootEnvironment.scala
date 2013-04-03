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

    env.create("%ENV", getHash(r.getSystem.getEnv.map(p => p._1 -> getStr(p._2)).toSeq:_*))
    
  }
}
