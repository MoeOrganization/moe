package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.Moe.Errors

class EnvironmentTestSuite extends FunSuite with BeforeAndAfter {

    var env : Environment = _

    before {
        env = new Environment()
    }

    test("... this is a root Environment") {
        assert( env.isRoot )
    }

    test("... there is no value for $foo") {
        assert( !env.has("$foo") )
    }

    test("... insert a value into $foo") {
        val c001 = new MoeObject()
        env.create( "$foo", c001 )
        assert( env.has("$foo") )
        assert( env.get("$foo") === c001 )
    }

    test("... overwrite a value into $foo") {
        val c001 = new MoeObject()
        val c002 = new MoeObject()

        env.create( "$foo", c001 )

        assert( env.has("$foo") )
        assert( env.get("$foo") === c001 )

        env.set( "$foo", c002 )

        assert( env.get("$foo") != c001 )
        assert( env.get("$foo") === c002 )
    }

    test("... value not found thrown") {
        val ex = intercept[Errors.ValueNotFound] {
            env.get( "$bar" )
        }
        assert( ex.getMessage === "$bar" )
    }

    test("... undefined value thrown") {
        val c001 = new MoeObject()
        val ex = intercept[Errors.UndefinedValue] {
            env.set( "$baz", c001 )
        }
        assert( ex.getMessage === "$baz" )
    }
 
}
