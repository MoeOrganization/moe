package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup package CORE
  */
object CorePackage {

  def apply(r: MoeRuntime): Unit = {
    val pkg = r.getCorePackage  
    val env = pkg.getEnv   

    import r.NativeObjects._

    pkg.addSubroutine(
      new MoeSubroutine(
        "say",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        { (e) => 
            r.say(e.getAs[MoeArrayObject]("@_").get.unboxToArrayBuffer.get:_*)
            getUndef
        }
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "print",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        { (e) => 
            r.print(e.getAs[MoeArrayObject]("@_").get.unboxToArrayBuffer.get:_*)
            getUndef
        }
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "warn",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        { (e) => 
            r.warn(e.getAs[MoeArrayObject]("@_").get.unboxToArrayBuffer.get:_*)
            getUndef
        }
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "eval",
        new MoeSignature(List(new MoePositionalParameter("$source"))),
        env,
        (e) => r.eval(e.getAs[MoeStrObject]("$source").get.unboxToString.get, e)
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "chr",
        new MoeSignature(List(new MoePositionalParameter("$i"))),
        env,
        (e) => getStr(e.getAs[MoeIntObject]("$i").get.unboxToInt.get.toChar.toString)
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "ord",
        new MoeSignature(List(new MoePositionalParameter("$i"))),
        env,
        (e) => getInt(e.getAs[MoeStrObject]("$i").get.unboxToString.get.charAt(0).toInt)
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "die",
        new MoeSignature(List(new MoeSlurpyParameter("@msg"))),
        env,
        (e) => throw new MoeErrors.MoeProblems(
          e.getAs[MoeArrayObject]("@msg").get.join(r).unboxToString.get
        )
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "sleep",
        new MoeSignature(List(new MoePositionalParameter("$seconds"))),
        env,
        { (e) => 
            val seconds = e.getAs[MoeIntObject]("$seconds").get.unboxToInt.get * 1000
            try {
              Thread.sleep(seconds);
            } catch {
                case e: InterruptedException => Thread.currentThread().interrupt()
            }
            getUndef
        }
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "exit",
        new MoeSignature(List(new MoeOptionalParameter("$status"))),
        env,
        { (e) => 
            val status = e.get("$status").get
            if (status.isUndef) {
              r.getSystem.exit()
            } else {
              r.getSystem.exit(status.unboxToInt.get)
            }
            getUndef
        }
      )
    )

  }
}
