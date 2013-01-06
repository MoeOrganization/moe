package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.Moe.Errors

class MoeClassTestSuite extends FunSuite with BeforeAndAfter {

    test("... test the whole thing together") {
        val klass = new MoeClass( "TestClass", "0.01", "cpan:STEVAN" )
        klass.addMethod( new MoeMethod( "ident", ( inv, args ) => inv ) )
        var obj = new MoeObject( klass )
        assert(  obj.callMethod( "ident" ) === obj )
    }

    test("... test makeInstance version") {
        val klass = new MoeClass( "TestClass", "0.01", "cpan:STEVAN" )
        klass.addMethod( new MoeMethod( "ident", ( inv, args ) => inv ) )
        var obj = klass.newInstance()
        assert(  obj.callMethod( "ident" ) === obj )
    }

}
