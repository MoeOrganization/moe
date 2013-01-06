package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeObjectTestSuite extends FunSuite with BeforeAndAfter {

    var o : MoeObject = _

    before {
        o = new MoeObject()
    }

    test("... all MoeObjects have an id") {
        assert( o.getID != null )
    }

    test("... MoeObjects do not have a default class") {
        assert( !o.hasAssociatedClass )
    }

    test("... MoeObjects do not have default values") {
        assert( !o.hasValue( "$.foo" ) )
    }

    test("... set a value in the instance") {
        val c001 = new MoeObject()
        o.setValue( "$.foo", c001 )
        assert( o.hasValue("$.foo") )
        assert( o.getValue("$.foo") === c001 )
    }

    test("... overwrite a value in the instance") {
        val c001 = new MoeObject()
        val c002 = new MoeObject()
        o.setValue( "$.foo", c001 )
        assert( o.hasValue("$.foo") )
        assert( o.getValue("$.foo") === c001 )

        o.setValue( "$.foo", c002 )
        assert( o.getValue("$.foo") != c001 )
        assert( o.getValue("$.foo") === c002 )
    }   

    test("... instance value not found thrown") {
        val ex = intercept[Runtime.Errors.InstanceValueNotFound] {
            o.getValue( "$.bar" )
        }
        assert( ex.getMessage === "$.bar" )
    }

    test("... missing class thrown") {
        intercept[Runtime.Errors.MissingClass] {
            o.callMethod( "foo", List() )
        }
    }

}
