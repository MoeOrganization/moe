package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeEnvironmentTestSuite extends FunSuite with BeforeAndAfter {

  var env : MoeEnvironment = _

  before {
    env = new MoeEnvironment()
  }

  test("... this is a root MoeEnvironment") {
    assert(env.isRoot)
  }

  test("... there is no value for $foo") {
    assert(!env.has("$foo"))
  }

  test("... insert a value into $foo") {
    val c001 = new MoeObject()
    env.create("$foo", c001)
    assert(env.has("$foo"))
    assert(env.get("$foo") === c001)
  }

  test("... overwrite a value into $foo") {
    val c001 = new MoeObject()
    val c002 = new MoeObject()

    env.create("$foo", c001)

    assert(env.has("$foo"))
    assert(env.get("$foo") === c001)

    env.set("$foo", c002)

    assert(env.get("$foo") != c001)
    assert(env.get("$foo") === c002)
  }

  test("... value not found thrown") {
    val ex = intercept[Runtime.Errors.ValueNotFound] {
        env.get("$bar")
    }
    assert(ex.getMessage === "$bar")
  }

  test("... undefined value thrown") {
    val c001 = new MoeObject()
    val ex = intercept[Runtime.Errors.UndefinedValue] {
      env.set("$baz", c001)
    }
    assert(ex.getMessage === "$baz")
  }

  test("... child changes parent") {
    val child = new MoeEnvironment(env)

    // Make sure the child knows it's a child
    assert(!child.isRoot)

    val c001 = new MoeObject()
    val c002 = new MoeObject()

    // Add $foo to the parent
    env.create("$foo", c001)
    // Verify env knows it has a $foo
    assert(env.has("$foo"))
    // And that the child has it as well
    assert(child.has("$foo"))

    // Verify that the child reaches into the parent for $foo
    assert(child.get("$foo") === c001)

    // Verify that the child & parent work for a missing value
    val ex = intercept[Runtime.Errors.UndefinedValue] {
      env.set("$baz", c001)
    }
    assert(ex.getMessage === "$baz")

    // Set $foo in the child, changing the parent
    child.set("$foo", c002)
    // Check the child's first
    assert(child.get("$foo") != c001)
    assert(child.get("$foo") === c002)
    // Now the parent
    assert(env.get("$foo") != c001)
    assert(env.get("$foo") === c002)
  }

  test("... child overrides parent") {
    val child = new MoeEnvironment(env)

    // Make sure the child knows it's a child
    assert(!child.isRoot)

    val c001 = new MoeObject()
    val c002 = new MoeObject()

    // Add $foo to the parent
    env.create("$foo", c001)
    // Verify env knows it has a $foo
    assert(env.has("$foo"))
    // And that the child has it as well
    assert(child.has("$foo"))

    // Verify that the child reaches into the parent for $foo
    assert(child.get("$foo") === c001)

    // Set $foo in the child, leaving the parent
    child.create("$foo", c002)
    // Check the child's first
    assert(child.get("$foo") != c001)
    assert(child.get("$foo") === c002)
    // Now the parent
    assert(env.get("$foo") != c002)
    assert(env.get("$foo") === c001)
  }

}
