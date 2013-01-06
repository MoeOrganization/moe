package org.moe.runtime

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoePackageTestSuite extends FunSuite with BeforeAndAfter {

    test("... basic package") {
        var pkg = new MoePackage( "main" )
        assert( pkg.getName() === "main" )
    }  
    
}