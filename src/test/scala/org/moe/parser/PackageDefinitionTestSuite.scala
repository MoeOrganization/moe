package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class PackageDefinitionTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a basic package block") {
    val result = interpretCode("""
      package foo::bar {
      }
    """)
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }
}
