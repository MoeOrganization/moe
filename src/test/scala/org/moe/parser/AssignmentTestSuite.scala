package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class AssignmentTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... a basic assignment") {
    val result = interpretCode("my $foo = 10; sub foo { $foo = $foo + 10; } foo()")
    result.unboxToInt.get should equal (20)
  }

}
