package org.moe.parser

import scala.util.parsing.combinator._
import scala.util.matching.Regex

import org.moe.ast._
import org.moe.runtime._

object MoeStringParser extends MoeQuoteParser {
  override def skipWhitespace = false

  private def string: Parser[AST] = rep(stringPart) ^^ { xs => StringSequenceNode(xs) }

  private def stringPart: Parser[AST] = (
      block
    | array
    | hash
    | subroutineCall
    | methodCall
    | arrayName
    | hashName
    | subroutineName
    | methodName
    | scalar
    | chars
  )

  private def block = quoted('{') ^^ EvalExpressionNode

  private def array = ("@" ~> ident) ~ quoted('[') ^^ {
    case name ~ ""    => EvalExpressionNode("@" + name)
    case name ~ index => EvalExpressionNode("@" + name + "[" + index + "]")
  }
  private def arrayName = ("@" ~> ident) ^^ {name => StringLiteralNode("@" + name) }

  private def hash = ("%" ~> ident) ~ quoted('{') ^^ {
    case name ~ ""    => EvalExpressionNode("%" + name)
    case name ~ index => EvalExpressionNode("%" + name + "{" + index + "}")
  }
  private def hashName = ("%" ~> ident) ^^ {name => StringLiteralNode("%" + name) }

  private def subroutineCall = ("&" ~> ident) ~ quoted('(') ^^ {
    case name ~ args => EvalExpressionNode("&" + name + "(" + args + ")")
  }
  private def subroutineName = ("&" ~> ident) ^^ {name => StringLiteralNode("&" + name) }

  private def methodCall = ("$" ~> ident) ~ ("." ~> ident) ~ quoted('(') ^^ {
    case obj ~ method ~ args => EvalExpressionNode("$" + obj + "." + method + "(" + args + ")")
  }
  private def methodName = ("$" ~> ident) ~ ("." ~> ident) ^^ {
    case obj ~ method => StringLiteralNode("$" + obj + "." + method)
  }

  private def scalar = ("$" ~> ident) ^^ {
    case name => VariableAccessNode("$" + name)
  }

  private def chars = """(\\.|[^$@%&{])+""".r ^^ StringLiteralNode

  private def getEntryPoint: Parser[AST] = string

  def interpolateStr(input: String): StringSequenceNode = {
    def error_msg(msg: String, next: Input) = "[" + next.pos + "] error: " + msg + "\n\n" + next.pos.longString

    parseAll(getEntryPoint, input) match {
      case Success(result, _) => result.asInstanceOf[StringSequenceNode]
      case NoSuccess(msg, next) => if (next.atEnd)
                                    throw new MoeErrors.ParserInputIncomplete(error_msg(msg, next))
                                  else
                                    throw new MoeErrors.ParserInputError(error_msg(msg, next))
    }
  }

  // helper method to test with any parser combinator
  def testParser(input: String, parser: Parser[AST] = getEntryPoint): AST = {
    def error_msg(msg: String, next: Input) = "[" + next.pos + "] error: " + msg + "\n\n" + next.pos.longString + ", offset = " + next.offset + ", source = " + next.source + ", first = [" + next.first + "], rest = [" + next.rest.source + "]"

    parseAll(parser, input) match {
      case Success(result, _)   => result
      case NoSuccess(msg, next) => if (next.atEnd)
                                    throw new MoeErrors.ParserInputIncomplete(error_msg(msg, next))
                                  else
                                    throw new MoeErrors.ParserInputError(error_msg(msg, next))
    }
  }

}
