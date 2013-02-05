package org.moe.parser

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

trait ParserTestUtils extends FunSuite with BeforeAndAfter  {

  var runtime : MoeRuntime = _

  before {
    runtime = new MoeRuntime()
    runtime.bootstrap()
  }

  def basicAST(nodes: StatementsNode) =
    CompilationUnitNode(ScopeNode(nodes))

  def interpretCode(code: String): MoeObject = {
    val ast = basicAST(Parser.parseStuff(code))
    Interpreter.eval(runtime, runtime.getRootEnv, ast)
  }

}
