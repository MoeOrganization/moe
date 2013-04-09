package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ArrayHashIndexingTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  // arrays

  test("... basic test with a simple array (indexing 0)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[0]")

    assert(result.unboxToInt.get === 1)
  }

  test("... basic test with a simple array (indexing 1)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[1]")

    assert(result.unboxToInt.get === 2)
  }

  test("... basic test with a simple array (indexing 2)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[2]")

    assert(result.unboxToInt.get === 3)
  }

  test("... basic test with a simple array (indexing -1)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[-1]")

    assert(result.unboxToInt.get === 3)
  }

  test("... basic test with a simple array (indexing -2)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[-2]")

    assert(result.unboxToInt.get === 2)
  }

  test("... basic test with a simple array (indexing 3)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[3]")

    assert(result.isUndef)
  }

  test("... basic test with a simple array element assignment (existing element)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[2] = 42; @x")

    val array = result.unboxToArrayBuffer.get
    assert(array.length === 3)
    assert(array(2).unboxToInt.get === 42)
  }

  test("... basic test with a simple array element assignment (new element)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[4] = 42; @x")

    val array = result.unboxToArrayBuffer.get
    assert(array.length === 5)
    assert(array(3).isUndef)
    assert(array(4).unboxToInt.get === 42)
  }

  // hashes

  test("... basic test with a simple hash (indexing one w/ double quotes)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"one"} """)

    assert(result.unboxToInt.get === 1)
  }

  test("... basic test with a simple hash (indexing one w/ single quotes)") {
    val result = interpretCode(" my %x = { one => 1, two => 2 }; %x{'one'}")

    assert(result.unboxToInt.get === 1)
  }

  test("... basic test with a simple hash (indexing two)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"two"} """)

    assert(result.unboxToInt.get === 2)
  }

  test("... basic test with a simple hash (indexing three)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"three"} """)

    assert(result.isUndef)
  }

  test("... basic test with a simple hash assignment (existing key)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"one"} = "one"; """)

    assert(result.unboxToString.get === "one")
  }

  test("... basic test with a simple hash assignment (new key)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"three"} = 3; """)

    assert(result.unboxToInt.get === 3)
  }

  // Both

  test("... basic test with a complex example (hash index providing array index)") {
    val result = interpretCode(""" my @x = [1, 2, 3]; my %x = { one => 1, two => 2 }; @x[ %x{"two"} ] """)

    assert(result.unboxToInt.get === 3)
  }

  test("... basic test with a complex example (array index providing array index)") {
    val result = interpretCode(""" my @x = [1, 2, 3]; my @y = [ 1, 2]; @x[ @y[1] ] """)

    assert(result.unboxToInt.get === 3)
  }

  test("... basic test with a complex example (hash index providing hash index)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; my %y = { '1' => "one", '2' => "two" }; %x{ %y{"2"} } """)

    assert(result.unboxToInt.get === 2)
  }

}
