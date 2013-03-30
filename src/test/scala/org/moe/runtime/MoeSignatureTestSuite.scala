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
    val param = new MoePositionalParameter("$x")
    val sig = new MoeSignature(List(param))

    val params = sig.getParams
    assert(params(0) === param)
  }

  test("... basic signature binding") {
    val sig = new MoeSignature(List(new MoePositionalParameter("$x")))

    val arg  = r.NativeObjects.getInt(5)
    val args = new MoeArguments(List(arg))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg))
  }

  test("... more signature binding") {
    val sig = new MoeSignature(List(
        new MoePositionalParameter("$x"),
        new MoePositionalParameter("$y")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val arg2 = r.NativeObjects.getInt(10)
    val args = new MoeArguments(List(arg1, arg2))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg1))

    assert(env.has("$y") === true)
    env.get("$y") should be (Some(arg2))    
  }

  test("... signature binding with optional") {
    val sig = new MoeSignature(List(
        new MoePositionalParameter("$x"),
        new MoeOptionalParameter("$y")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val args = new MoeArguments(List(arg1))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg1))

    assert(env.has("$y") === true)
    env.get("$y") should be (Some(r.NativeObjects.getUndef))
  }

  test("... signature binding with optional satisfied") {
    val sig = new MoeSignature(List(
        new MoePositionalParameter("$x"),
        new MoeOptionalParameter("$y")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val arg2 = r.NativeObjects.getInt(10)
    val args = new MoeArguments(List(arg1, arg2))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$x") === true)
    env.get("$x") should be (Some(arg1))

    assert(env.has("$y") === true)
    env.get("$y") should be (Some(arg2))    
  }

  test("... signature binding with slurpy") {
    val sig = new MoeSignature(List(
        new MoePositionalParameter("$h"),
        new MoeSlurpyParameter("@t")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val arg2 = r.NativeObjects.getInt(10)
    val arg3 = r.NativeObjects.getInt(15)
    val arg4 = r.NativeObjects.getInt(20)
    val args = new MoeArguments(List(arg1, arg2, arg3, arg4))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$h") === true)
    env.get("$h") should be (Some(arg1))

    assert(env.has("@t") === true)

    val a = env.get("@t").get.unboxToArrayBuffer.get
    assert(a.length === 3)
    assert(a(0) === arg2)
    assert(a(1) === arg3)
    assert(a(2) === arg4)
  }

  test("... signature binding with empty slurpy") {
    val sig = new MoeSignature(List(
        new MoePositionalParameter("$h"),
        new MoeSlurpyParameter("@t")
    ))

    val arg1 = r.NativeObjects.getInt(5)
    val args = new MoeArguments(List(arg1))

    val env = r.getRootEnv

    sig.bindArgsToEnv(args, env)

    assert(env.has("$h") === true)
    env.get("$h") should be (Some(arg1))

    assert(env.has("@t") === true)

    val a = env.get("@t").get.unboxToArrayBuffer.get
    assert(a.length === 0)
  }

}