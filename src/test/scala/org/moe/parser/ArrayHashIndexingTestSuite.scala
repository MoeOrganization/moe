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

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 1);
  }

  test("... basic test with a simple array (indexing 1)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[1]")

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2);
  }

  test("... basic test with a simple array (indexing 2)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[2]")

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3);
  }

  test("... basic test with a simple array (indexing -1)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[-1]")

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3);
  }

  test("... basic test with a simple array (indexing -2)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[-2]")

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2);
  }

  test("... basic test with a simple array (indexing 3)") {
    val result = interpretCode("my @x = [1, 2, 3]; @x[3]")

    assert(result.asInstanceOf[MoeNullObject] === runtime.NativeObjects.getUndef);
  }

  // hashes

  test("... basic test with a simple hash (indexing one w/ double quotes)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"one"} """)

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 1);
  }

  test("... basic test with a simple hash (indexing one w/ single quotes)") {
    val result = interpretCode(" my %x = { one => 1, two => 2 }; %x{'one'}")

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 1);
  }

  test("... basic test with a simple hash (indexing two)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"two"} """)

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2);
  }

  test("... basic test with a simple hash (indexing three)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; %x{"three"} """)

    assert(result.asInstanceOf[MoeNullObject] === runtime.NativeObjects.getUndef);
  }

  // Both

  test("... basic test with a complex example (hash index providing array index)") {
    val result = interpretCode(""" my @x = [1, 2, 3]; my %x = { one => 1, two => 2 }; @x[ %x{"two"} ] """)

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3);
  }

  test("... basic test with a complex example (hash index providing hash index)") {
    val result = interpretCode(""" my %x = { one => 1, two => 2 }; my %y = { '1' => "one", '2' => "two" }; %x{ %y{"2"} } """)

    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2);
  }

}
