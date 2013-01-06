package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeAttributeTestSuite extends FunSuite with BeforeAndAfter {

    test("... basic attribute") {
        val default = new MoeObject()
        val attr    = new MoeAttribute( "Foo", default )

        assert( attr.getName() == "Foo" )
        assert( attr.getDefault() === default )
    }    

}