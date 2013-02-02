package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeEnvironmentTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

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
    env.create("$foo", c001) should be (Some(c001))
    assert(env.has("$foo"))
    env.get("$foo") should be (Some(c001))
  }

  test("... overwrite a value into $foo") {
    val c001 = new MoeObject()
    val c002 = new MoeObject()

    env.create("$foo", c001) should be (Some(c001))

    assert(env.has("$foo"))
    env.get("$foo") should be (Some(c001))

    env.set("$foo", c002) should be (Some(c002))

    env.get("$foo") should be (Some(c002))
    env.get("$foo") should not be (Some(c001))
  }

  test("... value not found") {
    env.get("$bar") should be (None)
  }

  test("... undefined variable") {
    val c001 = new MoeObject()
    env.set("$baz", c001) should be (None)
  }

  test("... child changes parent") {
    val child = new MoeEnvironment(Some(env))

    // Make sure the child knows it's a child
    assert(!child.isRoot)
    child.getParent should be (Some(env))

    val c001 = new MoeObject()
    val c002 = new MoeObject()

    // Add $foo to the parent
    env.create("$foo", c001) should be (Some(c001))
    // Verify env knows it has a $foo
    assert(env.has("$foo"))
    // And that the child has it as well
    assert(child.has("$foo"))

    // Verify that the child reaches into the parent for $foo
    child.get("$foo") should be (Some(c001))

    // Verify that the child & parent work for a missing value
    env.set("$baz", c001) should be (None)
    child.set("$baz", c001) should be (None)

    // Set $foo in the child, changing the parent
    child.set("$foo", c002) should be (Some(c002))
    // Check the child's first
    child.get("$foo") should not be (Some(c001))
    child.get("$foo") should be (Some(c002))
    // Now the parent
    env.get("$foo") should not be (Some(c001))
    env.get("$foo") should be (Some(c002))
  }

  test("... child overrides parent") {
    val child = new MoeEnvironment(Some(env))

    // Make sure the child knows it's a child
    assert(!child.isRoot)
    child.getParent should be (Some(env))

    val c001 = new MoeObject()
    val c002 = new MoeObject()

    // Add $foo to the parent
    env.create("$foo", c001) should be (Some(c001))
    // Verify env knows it has a $foo
    assert(env.has("$foo"))
    // And that the child has it as well
    assert(child.has("$foo"))

    // Verify that the child reaches into the parent for $foo
    child.get("$foo") should be (Some(c001))

    // Set $foo in the child, leaving the parent
    child.create("$foo", c002) should be (Some(c002))
    // Check the child's first
    child.get("$foo") should not be (Some(c001))
    child.get("$foo") should be (Some(c002))
    // Now the parent
    env.get("$foo") should not be (Some(c002))
    env.get("$foo") should be (Some(c001))
  }

  test("... getCurrentPackage") {
    env.getCurrentPackage should be (None)
  }

  test("... setCurrentPackage") {
    val pkg = new MoePackage("Test", env)
    env.setCurrentPackage(pkg)
    env.getCurrentPackage should be (Some(pkg))
  }

  test("... getCurrentClass") {
    env.getCurrentClass should be (None)
  }

  test("... setCurrentClass") {
    val klass = new MoeClass("Test")
    env.setCurrentClass(klass)
    env.getCurrentClass should be (Some(klass))
  }

  test("... getCurrentInvocant") {
    env.getCurrentInvocant should be (None)
  }

  test("... setCurrentInvocant") {
    val inv = new MoeObject()
    env.setCurrentInvocant(inv)
    env.getCurrentInvocant should be (Some(inv))
  }

  test("... getCurrentTopic") {
    env.getCurrentTopic should be (None)
  }

  test("... setCurrentTopic") {
    val t = new MoeObject()
    env.setCurrentTopic(t)
    env.getCurrentTopic should be (Some(t))
  }

}
