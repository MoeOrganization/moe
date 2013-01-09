package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.collection.mutable.HashMap

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

    test("... simple Array object") {
        val o = new MoeArrayObject( 
            List( 
                new MoeNullObject(),
                new MoeIntObject( 10 )
            ) 
        )
        val array = o.getNativeValue()
        assert( array(0).asInstanceOf[ MoeNullObject ].getNativeValue() === null )
        assert( array(1).asInstanceOf[ MoeIntObject ].getNativeValue() === 10 )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... false Array object") {
        val o = new MoeArrayObject( List() )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... simple Hash object") {
        val o = new MoeHashObject( 
            HashMap( 
                "foo" -> new MoeNullObject(),
                "bar" -> new MoeIntObject( 10 )
            ) 
        )
        val hash = o.getNativeValue()
        assert( hash("foo").asInstanceOf[ MoeNullObject ].getNativeValue() === null )
        assert( hash("bar").asInstanceOf[ MoeIntObject ].getNativeValue() === 10 )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... false Hash object") {
        val o = new MoeHashObject( HashMap() )
        assert( !o.isTrue() )
        assert( o.isFalse() )
        assert( !o.isUndef() )        
    }

    test("... simple Pair object") {
        val o = new MoePairObject( "foo" -> new MoeIntObject( 10 ) )
        val pair = o.getNativeValue()
        assert( pair._1 === "foo" )
        assert( pair._2.asInstanceOf[ MoeIntObject ].getNativeValue() === 10 )
        assert( o.isTrue() )
        assert( !o.isFalse() )
        assert( !o.isUndef() )        
    }

}
