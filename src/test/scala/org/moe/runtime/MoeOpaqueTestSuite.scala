package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeOpaqueTestSuite extends FunSuite with BeforeAndAfter {

    var o : MoeOpaque = _

    before {
        o = new MoeOpaque()
    }

    test("... MoeOpaques do not have default values") {
        assert(!o.hasValue("$.foo"))
    }

    test("... set a value in the instance") {
        val c001 = new MoeOpaque()
        o.setValue("$.foo", c001)
        assert(o.hasValue("$.foo"))
        assert(o.getValue("$.foo") === c001)
    }

    test("... overwrite a value in the instance") {
        val c001 = new MoeOpaque()
        val c002 = new MoeOpaque()
        o.setValue("$.foo", c001)
        assert(o.hasValue("$.foo"))
        assert(o.getValue("$.foo") === c001)

        o.setValue("$.foo", c002)
        assert(o.getValue("$.foo") != c001)
        assert(o.getValue("$.foo") === c002)
    }

    test("... instance value not found thrown") {
        val ex = intercept[Runtime.Errors.InstanceValueNotFound] {
            o.getValue("$.bar")
        }
        assert(ex.getMessage === "$.bar")
    }

}
