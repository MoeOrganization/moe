package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeSubroutineTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... basic sub") {
    val env    = r.getRootEnv
    val value  = new MoeObject(Some(MoeScalarType(None)))
    val sub    = new MoeSubroutine(
      name            = "ident", 
      signature       = new MoeSignature(List(new MoePositionalParameter("$x"))),
      declaration_env = env,
      body            = (e) => e.get("$x").get
    )
    val result = sub.execute(new MoeArguments(List(value)))
    assert(result === value)
  }

  test("... not so basic sub") {

    val sub = new MoeSubroutine(
      name            = "adder", 
      declaration_env = r.getRootEnv,
      signature       = new MoeSignature(List(
        new MoePositionalParameter("$x"),
        new MoePositionalParameter("$y")
      )),
      body = { e => 
        r.NativeObjects.getInt(
          e.get("$x").get.unboxToInt.get 
          + 
          e.get("$y").get.unboxToInt.get
        )
      }
    )
    val result = sub.execute(
      new MoeArguments(List(
        r.NativeObjects.getInt(1),
        r.NativeObjects.getInt(1)
      ))
    )
    assert(result.unboxToInt.get === 2)
  }

}