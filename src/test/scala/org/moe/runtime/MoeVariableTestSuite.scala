package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeVariableTestSuite extends FunSuite with BeforeAndAfter {

    test("... basic variable") {
        val value    = new MoeObject()
        val variable = new MoeVariable( "Foo", value )

        assert( variable.getName() == "Foo" )
        assert( variable.getValue() === value )
    }    

    test("... basic variable mutation") {
        val value1   = new MoeObject()
        val value2   = new MoeObject()
        val variable = new MoeVariable( "Foo", value1 )

        assert( variable.getName() == "Foo" )
        assert( variable.getValue() === value1 )

        variable.setValue( value2 )

        assert( variable.getValue() === value2 )
    }    

}