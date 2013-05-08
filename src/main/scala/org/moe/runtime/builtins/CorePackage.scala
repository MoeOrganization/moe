package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.collection.JavaConversions._

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
        (e) => try {
            e.set("$!", getUndef)
            e.setCurrentPackage(r.getRootPackage)
            r.eval(e.getAs[MoeStrObject]("$source").get.unboxToString.get, e)
          } catch {
            case exception: Exception => {
              //exception.printStackTrace()
              e.set("$!", getException(exception.toString()))
              getUndef
            }
          } finally {
            e.setCurrentPackage(r.getCorePackage)
          }
      )
    )

    pkg.addSubroutine(
      new MoeSubroutine(
        "not",
        new MoeSignature(List(new MoePositionalParameter("$i"))),
        env,
        (e) => if (e.getAs[MoeObject]("$i").get.unboxToBoolean.get) getFalse else getTrue
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
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        (e) => throw new MoeErrors.MoeProblems(
          e.getAs[MoeArrayObject]("@_").get.join(r).unboxToString.get
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

    // [ $package, $class, $sub_or_method_name, $invocant, @args ]
    pkg.addSubroutine(
      new MoeSubroutine(
        "caller",
        new MoeSignature(List(new MoeOptionalParameter("$index"))),
        env,
        (e) => e.get("$index") match {
          case Some(i: MoeIntObject) => try { 
            r.convertInterpreterStackFrame(r.getInterpreterCallStack.drop(1).get(i.unboxToInt.get))
          } catch { 
            case e: java.lang.IndexOutOfBoundsException => getUndef 
          }
          case _ => getArray(r.getInterpreterCallStack.drop(1).map(r.convertInterpreterStackFrame(_)):_*)
        } 
      )
    )

    /**
     * NOTE:
     * This is extremely naive and does not capture 
     * all the complexity of the true c<system> command
     * in Perl, however it is something to start with.
     */
    pkg.addSubroutine(
      new MoeSubroutine(
        "system",
        new MoeSignature(List(new MoeSlurpyParameter("@_"))),
        env,
        { (e) => 
          val cmd = bufferAsJavaList(e.getAs[MoeArrayObject]("@_").get.unboxToArrayBuffer.get.map(_.unboxToString.get))
          val pb  = new ProcessBuilder(cmd);
          val env = pb.environment();
          env.clear()
          e.getAs[MoeHashObject]("%ENV").get.unboxToMap.get.map({
            case (k, v) => env.put(k, v.unboxToString.get)
          })
          val p   = pb.start()
          val err = new java.io.DataInputStream(p.getErrorStream())
          p.waitFor()
          val err_out = new java.io.BufferedReader(new java.io.InputStreamReader(err));
          var x = err_out.readLine()
          var err_txt = ""
          while (x != null) {
            err_txt = err_txt + x
            x = err_out.readLine()
          }
          e.set("$OS_ERROR", if (err_txt == null) getUndef else getStr(err_txt))
          getInt(p.exitValue)
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

    pkg.addSubroutine(
      new MoeSubroutine(
        "rand",
        new MoeSignature(List(new MoeOptionalParameter("$limit"))),
        env,
        { (e) =>
          val limit = e.get("$limit") match {
            case Some(none:  MoeUndefObject) => 1.0
            case Some(value: MoeObject)      => value.unboxToDouble.get
            case _                           => 1.0
          }
          getNum(limit * scala.util.Random.nextDouble)
        }
      )
    )
  }
}
