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

}
