package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeNativeObjectTestSuite extends FunSuite with BeforeAndAfter {

    test("... simple String object") {
        val o = new MoeStringObject( "Hello World" )
        assert( o.getNativeValue() === "Hello World" )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )
    }

    test("... false String object") {
        val o = new MoeStringObject( "" )
        assert( o.getNativeValue() === "" )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )
    }

    test("... simple Int object") {
        val o = new MoeIntObject( 10 )
        assert( o.getNativeValue() === 10 )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... false Int object") {
        val o = new MoeIntObject( 0 )
        assert( o.getNativeValue() === 0 )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... simple Float object") {
        val o = new MoeFloatObject( 10.5 )
        assert( o.getNativeValue() === 10.5 )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )
    }

    test("... false Float object") {
        val o = new MoeFloatObject( 0.0 )
        assert( o.getNativeValue() === 0.0 )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )
    }

    test("... simple Boolean object") {
        val o = new MoeBooleanObject( true )
        assert( o.getNativeValue() === true )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... false Boolean object") {
        val o = new MoeBooleanObject( false )
        assert( o.getNativeValue() === false )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... simple Null object") {
        val o = new MoeNullObject()
        assert( o.getNativeValue() === null )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( o.isUndef() )        
    }

}
