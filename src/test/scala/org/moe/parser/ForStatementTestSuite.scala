package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ForStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a for block") {
    val result = interpretCode("my @a = [1,2,3]; my $sum = 0; for (my $i = 0; $i < @a.length; $i = $i + 1) { $sum = $sum + @a[$i]  } $sum")
    assert(result.unboxToInt.get === 6)
  }

}
