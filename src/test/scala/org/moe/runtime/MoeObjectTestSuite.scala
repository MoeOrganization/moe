package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeObjectTestSuite extends FunSuite with BeforeAndAfter {

    var o : MoeObject = _

    before {
        o = new MoeObject()
    }

    test("... all MoeObjects have an id") {
        assert(o.getID != null)
    }

    test("... MoeObjects do not have a default class") {
        assert(!o.hasAssociatedClass)
    }

    test("... missing class thrown") {
        intercept[Runtime.Errors.MissingClass] {
            o.callMethod("foo", List())
        }
    }

}
