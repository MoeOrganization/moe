package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._
import org.moe.runtime._

import ParserUtils._

object MoeParsers extends Statements {
  def getEntryPoint: Parser[AST] = statements

  // Parser wrapper -- indicates the start node
  def parseFromEntry(input: String): StatementsNode =
    parseAll(getEntryPoint, input) match {
      case Success(result, _) => result.asInstanceOf[StatementsNode]
      case failure : NoSuccess => if (failure.next.atEnd)
                                    throw new MoeErrors.ParserInputIncomplete(failure.msg)
                                  else
                                    throw new MoeErrors.ParserInputError(failure.msg)
    }
}

object Parser {
  def parseStuff(input: String): StatementsNode = MoeParsers.parseFromEntry(input)
}
