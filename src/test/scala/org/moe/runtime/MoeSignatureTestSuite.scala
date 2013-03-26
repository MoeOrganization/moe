package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeSignatureTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... basic signature") {
    val param = new MoeNamedParameter("$x")
    val sig = new MoeSignature(List(param))

    val params = sig.getParams
    assert(params(0) === param)
  }

  test("... basic signature binding") {
    val sig = new MoeSignature(List(new MoeNamedParameter("$x")))

    val arg  = r.NativeObjects.getInt(5)
    val args = new MoeArguments(List(arg))

    val env = new MoeEnvironment()

    sig.checkArguments(args)
    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg))
  }

  test("... more signature binding") {
    val sig = new MoeSignature(List(
        new MoeNamedParameter("$x"),
        new MoeNamedParameter("$y")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val arg2 = r.NativeObjects.getInt(10)
    val args = new MoeArguments(List(arg1, arg2))

    val env = new MoeEnvironment()

    sig.checkArguments(args)
    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg1))

    assert(env.has("$y") === true)
    env.get("$y") should be (Some(arg2))    
  }

  test("... basic signature checking fail") {
    val sig  = new MoeSignature(List(new MoeNamedParameter("$x")))
    val args = new MoeArguments()

    intercept[MoeErrors.MoeProblems] {
        sig.checkArguments(args)
    }
  }  

}