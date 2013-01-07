package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNativeObjectTestSuite extends FunSuite with BeforeAndAfter {

    test("... simple null object") {
        val o = new MoeNativeObject( null )
        assert( o.getNativeValue() === null )
    }

    test("... simple String object") {
        val o = new MoeNativeObject( "Hello World" )
        assert( o.getNativeValue() === "Hello World" )
    }

    test("... simple Number object") {
        val o = new MoeNativeObject( 10 )
        assert( o.getNativeValue() === 10 )
    }

    test("... simple Boolean object") {
        val o = new MoeNativeObject( true )
        assert( o.getNativeValue() === true )
    }

    test("... test getValue exception thrown") {
        val o = new MoeNativeObject( null )
        intercept[ Runtime.Errors.MethodNotAllowed ] {
            o.getValue( "foo" )
        }
    }

    test("... test setValue exception thrown") {
        val o = new MoeNativeObject( null )
        intercept[ Runtime.Errors.MethodNotAllowed ] {
            o.setValue( "foo", new MoeObject() )
        }
    }

    test("... test hasValue exception thrown") {
        val o = new MoeNativeObject( null )
        intercept[ Runtime.Errors.MethodNotAllowed ] {
            o.hasValue( "foo" )
        }
    }

}
