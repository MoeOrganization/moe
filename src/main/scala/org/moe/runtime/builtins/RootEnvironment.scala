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

    /***************************************
    ** The Outside world
    ***************************************/

    env.create("@ARGV", getArray())        // command line arguments
    env.create("%ENV", getHash(r.getEnv))  // environment hash

    /***************************************
    ** The Internal world
    ***************************************/

    env.create("@INC", getArray(r.getIncludeDirs))  // the include directories
    env.create("%INC",  getHash())                  // mapping of packages to files they are contained in

    /**************************************
    ** The User Accessible world
    **************************************/

    env.create("$!", getUndef)    // universal exception objects
    env.create("$_", getUndef)    // the current topic object

    /***************************************/


    // SEE ALSO -> http://perlcabal.org/syn/S28.html
  }
}
