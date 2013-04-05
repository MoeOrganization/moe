package org.moe.runtime.nativeobjects

import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNativeObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... test simple native object dump output") {
    assert((r.NativeObjects.getInt(42)).toString == "42")
    assert((r.NativeObjects.getInt(-42)).toString == "-42")

    assert((r.NativeObjects.getNum(0.42)).toString == "0.42")
    assert((r.NativeObjects.getNum(-0.42)).toString == "-0.42")

    assert((r.NativeObjects.getTrue).toString == "true")
    assert((r.NativeObjects.getFalse).toString == "false")

    assert((r.NativeObjects.getStr("jason")).toString == "\"jason\"")
  }

  test("... test complex native object dump output") {
    val array = r.NativeObjects.getArray(
      r.NativeObjects.getInt(42),
      r.NativeObjects.getNum(98.6),
      r.NativeObjects.getStr("moe")
    )
    assert(array.toString == """[42, 98.6, "moe"]""")

    val hash = r.NativeObjects.getHash(
      "name" -> r.NativeObjects.getStr("moe"),
      "awesome" -> r.NativeObjects.getTrue
    )
    assert(
      hash.toString ==
        """{name => "moe", awesome => true}"""
    )

    val hashInArray = r.NativeObjects.getArray(
      r.NativeObjects.getInt(10),
      r.NativeObjects.getInt(20),
      hash
    )
    assert(
      hashInArray.toString ==
        """[10, 20, {name => "moe", awesome => true}]"""
    )

    val arrayInArray = r.NativeObjects.getArray(
      r.NativeObjects.getInt(10),
      r.NativeObjects.getInt(20),
      array
    )
    assert(arrayInArray.toString == """[10, 20, [42, 98.6, "moe"]]""")

    val arrayInHash = r.NativeObjects.getHash(
      "my_hash" -> array
    )
    assert(arrayInHash.toString == """{my_hash => [42, 98.6, "moe"]}""")

    val hashInHash = r.NativeObjects.getHash(
      "my_hash" -> hash
    )
    assert(
      hashInHash.toString ==
        """{my_hash => {name => "moe", awesome => true}}"""
    )
  }


}
