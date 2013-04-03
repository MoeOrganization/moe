package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class WhileUntilStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a simple while") {
    val result = interpretCode("my $x = 5; while ($x != 0) { $x = $x - 1; }; $x")
    assert(result.unboxToInt.get === 0)
  }

  test("... a simple until") {
    val result = interpretCode("my $x = 5; until ($x == 0) { $x = $x - 1; }; $x")
    assert(result.unboxToInt.get === 0)
  }

}
