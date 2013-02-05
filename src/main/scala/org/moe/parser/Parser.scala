package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._

import ParserUtils._

object MoeParsers extends Statements {
  def getEntryPoint: Parser[AST] = statements

  // Parser wrapper -- indicates the start node
  def parseFromEntry(input: String): StatementsNode =
    parseAll(getEntryPoint, input) match {
      case Success(result, _) => result.asInstanceOf[StatementsNode]
      case failure : NoSuccess => scala.sys.error(failure.msg)
    }
}

object Parser {
  def parseStuff(input: String): StatementsNode = MoeParsers.parseFromEntry(input)
}
