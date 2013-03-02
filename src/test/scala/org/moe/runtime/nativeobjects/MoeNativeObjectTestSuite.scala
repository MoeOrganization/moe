package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNativeObjectTestSuite extends FunSuite with BeforeAndAfter {

  test("... test simple native object dump output") {
    assert((new MoeIntObject(42)).toString == "42")
    assert((new MoeIntObject(-42)).toString == "-42")

    assert((new MoeNumObject(0.42)).toString == "0.42")
    assert((new MoeNumObject(-0.42)).toString == "-0.42")

    assert((new MoeBoolObject(true)).toString == "true")
    assert((new MoeBoolObject(false)).toString == "false")

    assert((new MoeStrObject("jason")).toString == "\"jason\"")
  }

  test("... test complex native object dump output") {
    val array = new MoeArrayObject(
      List(
        new MoeIntObject(42),
        new MoeNumObject(98.6),
        new MoeStrObject("moe")
      )
    )
    assert(array.toString == """[42, 98.6, "moe"]""")

    val hash = new MoeHashObject(
      HashMap(
        "name" -> new MoeStrObject("moe"),
        "awesome" -> new MoeBoolObject(true)
      )
    )
    assert(
      hash.toString ==
        """{name => "moe", awesome => true}"""
    )

    val hashInArray = new MoeArrayObject(
      List(
        new MoeIntObject(10),
        new MoeIntObject(20),
        hash
      )
    )
    assert(
      hashInArray.toString ==
        """[10, 20, {name => "moe", awesome => true}]"""
    )

    val arrayInArray = new MoeArrayObject(
      List(
        new MoeIntObject(10),
        new MoeIntObject(20),
        array
      )
    )
    assert(arrayInArray.toString == """[10, 20, [42, 98.6, "moe"]]""")

    val arrayInHash = new MoeHashObject(
      HashMap("my_hash" -> array)
    )
    assert(arrayInHash.toString == """{my_hash => [42, 98.6, "moe"]}""")

    val hashInHash = new MoeHashObject(
      HashMap("my_hash" -> hash)
    )
    assert(
      hashInHash.toString ==
        """{my_hash => {name => "moe", awesome => true}}"""
    )
  }


}
