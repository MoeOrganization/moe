package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._

import ParserUtils._

object Parser extends Statements {

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(statement, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

