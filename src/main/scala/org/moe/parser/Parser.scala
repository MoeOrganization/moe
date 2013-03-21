package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._
import org.moe.runtime._

import ParserUtils._

object MoeParsers extends Statements {
  def getEntryPoint: Parser[AST] = statements

  // Parser wrapper -- indicates the start node
  def parseFromEntry(input: String): StatementsNode = {
    def error_msg(msg: String, next: Input) = "[" + next.pos + "] error: " + msg + "\n\n" + next.pos.longString

    parseAll(getEntryPoint, input) match {
      case Success(result, _) => result.asInstanceOf[StatementsNode]
      case NoSuccess(msg, next) => if (next.atEnd)
                                    throw new MoeErrors.ParserInputIncomplete(error_msg(msg, next))
                                  else
                                    throw new MoeErrors.ParserInputError(error_msg(msg, next))
    }
  }
}

object Parser {
  def parseStuff(input: String): StatementsNode = MoeParsers.parseFromEntry(input)
}
