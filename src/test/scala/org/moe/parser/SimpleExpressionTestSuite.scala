package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... literal int addition") {
    val ast = basicAST(Parser.parseStuff("2+2"))
    println(Serializer.toJSON(ast))
    // Interpreter.eval(runtime, runtime.getRootEnv, ast)
  // }
    // val result = interpretCode("2 + 2")
    // assert(result.asInstanceOf[MoeIntObject].getNativeValue === 4)
  }


}
