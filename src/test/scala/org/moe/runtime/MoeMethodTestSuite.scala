package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeMethodTestSuite extends FunSuite with BeforeAndAfter {

    test("... basic method") {
        val invocant = new MoeObject()
        val method   = new MoeMethod( "ident", ( inv, args ) => inv )
        val result   = method.execute( invocant, List() )
        assert( result === invocant )
    }  

    test("... basic yadda-yadda-yadda method") {
        val invocant = new MoeObject()
        val method   = new MoeMethod( "yadda_yadda_yadda" )
        intercept[Runtime.Errors.UndefinedMethod] {  
            method.execute( invocant, List() )
        }    
    }
    
}