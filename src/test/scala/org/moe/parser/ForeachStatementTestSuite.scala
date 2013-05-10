package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ForeachStatementTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a foreach block") {
    val result = interpretCode("my @a = [1,2,3]; my $sum = 0; foreach (0..2) { $sum = $sum + @a[$_]  } $sum")
    assert(result.unboxToInt.get === 6)
  }

  test("... a for block with topic") {
    val result = interpretCode("my $sum = 0; for $x (1..3) { $sum = $sum + $x  } $sum")
    assert(result.unboxToInt.get === 6)
  }

  test("... a for block with multiple statements") {
    val result = interpretCode("my $result = 0; for $x (1..3) { $result = $result + $x; $result = $result * 2  } $result")
    assert(result.unboxToInt.get === 22)
  }

  test("... a for block to modify array") {
    val result = interpretCode("my @a = [1,2,3]; for (0..2) { @a[$_] = @a[$_] + 1  } @a.join(',')")
    assert(result.unboxToString.get === "2,3,4")
  }

  // XXXX: this should work when binding the topic variable to array
  // elements is implemented

  // test("... a for block to modify array (take 2)") {
  //   val result = interpretCode("my @a = [1,2,3]; for (@a) { $_ = $_ + 1  } @a.join(',')")
  //   assert(result.unboxToString.get === "2,3,4")
  // }
}
