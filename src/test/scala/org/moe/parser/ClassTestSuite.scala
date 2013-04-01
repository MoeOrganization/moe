package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class ClassTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  test("... a basic class") {
    val result = interpretCode("class Foo {}")
    result.asInstanceOf[MoeClass].getName should be ("Foo")
    result.asInstanceOf[MoeClass].getSuperclass should be (Some(runtime.getClassClass))
    result.asInstanceOf[MoeClass].getConstructor should be (None)
    result.asInstanceOf[MoeClass].getDestructor should be (None)
  }

  test("... a basic class w/ method") {
    val result = interpretCode("class Foo { method bar { 10 } }")
    result.asInstanceOf[MoeClass].getName should be ("Foo")
    result.asInstanceOf[MoeClass].hasMethod("bar") should be (true)
  }

  test("... a basic class w/ attribute") {
    val result = interpretCode("class Foo { has $.bar }")
    result.asInstanceOf[MoeClass].getName should be ("Foo")
    result.asInstanceOf[MoeClass].hasAttribute("$.bar") should be (true)
  }

  test("... a basic class w/ constructor") {
    val result = interpretCode("class Foo { BUILD {} }")
    result.asInstanceOf[MoeClass].getName should be ("Foo")
    result.asInstanceOf[MoeClass].hasConstructor should be (true)
  }

  test("... a basic class w/ destructor") {
    val result = interpretCode("class Foo { DEMOLISH {} }")
    result.asInstanceOf[MoeClass].getName should be ("Foo")
    result.asInstanceOf[MoeClass].hasDestructor should be (true)
  }

  test("... a basic class w/ instance creation") {
    val result = interpretCode("class Foo {}; ^Foo->new")
    result.asInstanceOf[MoeOpaque].isInstanceOf("Foo") should be (true)
    result.asInstanceOf[MoeOpaque].getAssociatedClass.get.getName should be ("Foo")
  }

  test("... a basic class w/ instance creation and method calling") {
    val result = interpretCode("class Foo { method bar { 10 } }; ^Foo->new->bar")
    result.unboxToInt.get should be (10)
  }

  test("... a basic class w/ instance creation and attribute init and access") {
    val result = interpretCode("class Foo { has $.bar = 10; method bar { $.bar } }; ^Foo->new->bar")
    result.unboxToInt.get should be (10)
  }

  private val attrDefault = """
    class Foo { 
      has $.bar = 0 .. 5; 
      method bar { $.bar->pop }; 
      method baz { $.bar } 
    }
  """

  test("... test that attribute defaults are cloned properly") {
    val result = interpretCode(attrDefault + "; my $f1 = ^Foo->new; my $f2 = ^Foo->new; [ $f1->bar, $f1->baz, $f2->bar, $f2->baz ]")
    result.toString should be ("[5, [0, 1, 2, 3, 4], 5, [0, 1, 2, 3, 4]]")
  }

  private val pointClass = """
    class Point {
      has $.x = 0;
      has $.y = 0;
      method x ($x?) { 
        if ($x) { $.x = $x };
        $.x
      };
      method y ($y?) { 
        if ($y) { $.y = $y };
        $.y
      };
    }
  """

  test("... a complex class (take 1)") {
    val result = interpretCode(pointClass + "; my $p = ^Point->new; $p->x")
    result.unboxToInt.get should be (0)
  }

  test("... a complex class (take 2)") {
    val result = interpretCode(pointClass + "; my $p = ^Point->new; $p->x(10); $p->x")
    result.unboxToInt.get should be (10)
  }

  private val pointClass_v2 = """
    class Point {
      has $.x = 0;
      has $.y = 0;
      BUILD ($x, $y) {
        $.x = $x;
        $.y = $y
      };
      method x ($x?) { 
        if ($x) { $.x = $x };
        $.x
      };
      method y ($y?) { 
        if ($y) { $.y = $y };
        $.y
      };
    }
  """

  test("... a complex class v2 (take 1)") {
    val result = interpretCode(pointClass_v2 + "; my $p = ^Point->new(10, 20); $p->x")
    result.unboxToInt.get should be (10)
  }

  private val greeterClass = """
    class Greater {
        has $.message = "Hello ";

        method greet ($place) {
            $.message . $place
        }
    };
  """
  test("... a complex class v3 (take 1)") {
    val result = interpretCode(greeterClass + "; ^Greater->new->greet(\"World\")->uc")
    result.unboxToString.get should be ("HELLO WORLD")
  }

}
