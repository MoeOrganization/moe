package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class PackageDefinitionTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a basic package block") {
    val result = interpretCode("package Foo {}")
    assert(result.asInstanceOf[MoePackage].getName === "Foo")
  }

  test("... simple package scoping") {
    val result = interpretCode("package Foo { sub bar {'BAR'} } Foo::bar()")
    assert(result.unboxToString.get === "BAR")
  }

  test("... nested package scoping") {
    val result = interpretCode("package Foo { package Bar { sub baz {'BAZ'} } } Foo::Bar::baz()")
    assert(result.unboxToString.get === "BAZ")
  }

  test("... complex package scoping") {
    val result = interpretCode("package Foo { package Bar { sub baz {'BAZ'} sub bar {baz()} } } Foo::Bar::bar()")
    assert(result.unboxToString.get === "BAZ")
  }

  test("... test automatic package tree creation") {
    val result = interpretCode("package Foo::Bar { sub baz {'BAZ'} }; Foo::Bar::baz()")
    assert(result.unboxToString.get === "BAZ")
  }

  test("... test automatic package tree creation doesn't clobber existing packages") {
    val result = interpretCode("package Foo { sub bar { 'BAR' } }; package Foo::Bar { sub baz { 'BAZ' } }; Foo::bar();")
    assert(result.unboxToString.get === "BAR")
  }

  test("... test automatic package tree creation doesn't clobber existing packages (again)") {
    val result = interpretCode("package Foo { sub bar {'BAR'} }; package Foo { sub baz {'BAZ'} }; Foo::bar() ~ Foo::baz()")
    assert(result.unboxToString.get === "BARBAZ")
  }

}
