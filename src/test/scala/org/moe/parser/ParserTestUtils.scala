package org.moe.parser

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

trait ParserTestUtils {

  def basicAST(nodes: List[AST]) =
    CompilationUnitNode(ScopeNode(StatementsNode(nodes)))

  def interpretCode(code: String): MoeObject = {
    val ast = basicAST(List(Parser.parseStuff(code)))
    Interpreter.eval(MoeRuntime.getRootEnv, ast)
  }

}
