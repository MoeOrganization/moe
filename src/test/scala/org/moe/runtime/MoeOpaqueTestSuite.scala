package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class MoeOpaqueTestSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

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
    o.getValue("$.foo") should be (Some(c001))
  }

  test("... overwrite a value in the instance") {
    val c001 = new MoeOpaque()
    val c002 = new MoeOpaque()
    o.setValue("$.foo", c001)
    assert(o.hasValue("$.foo"))
    o.getValue("$.foo") should be (Some(c001))

    o.setValue("$.foo", c002)
    o.getValue("$.foo") should be (Some(c002))
    o.getValue("$.foo") should not be (Some(c001))
  }

}
