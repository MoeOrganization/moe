package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.Moe.Errors

class MoeClassTestSuite extends FunSuite with BeforeAndAfter {

    test("... test the whole thing together") {
        
        val klass = new MoeClass( "Foo", "0.01", "cpan:STEVAN" )

        klass.addMethod(
            new MoeMethod(
                "bar",
                ( inv, args ) => inv
            )
        )

        var obj = new MoeObject( klass )

        assert( 
            obj.callMethod( "bar", List() )
            ===
            obj
        )

    }

}
