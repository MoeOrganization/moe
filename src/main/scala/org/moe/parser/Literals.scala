package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Literals extends Base {
  // Numeric literals
  def intNumber: Parser[IntLiteralNode]   =
    """[1-9][0-9_]*""".r ^^ { n => IntLiteralNode(formatInt(n)) }
  def octIntNumber: Parser[IntLiteralNode] =
    """0[0-9_]+""".r ^^ { n => IntLiteralNode(formatOctal(n)) }
  def hexIntNumber: Parser[IntLiteralNode] =
    """0x[0-9A-Fa-f_]+""".r ^^ { n => IntLiteralNode(formatHex(n)) }
  def binIntNumber: Parser[IntLiteralNode] =
    """0b[01_]+""".r ^^ { n => IntLiteralNode(formatBin(n)) }
  def floatNumber: Parser[FloatLiteralNode] =
    """[0-9_]*\.[0-9_]+([eE][\-+]?[0-9_]+)?""".r ^^ { n => FloatLiteralNode(formatFloat(n)) }

  // Boolean literals
  def constTrue: Parser[BooleanLiteralNode] =
    "true".r ^^^ BooleanLiteralNode(true)
  def constFalse: Parser[BooleanLiteralNode] =
    "false".r ^^^ BooleanLiteralNode(false)

  // String literals
  def doubleQuoteStringContent: Parser[StringLiteralNode] =
    """[^"]*""".r ^^ StringLiteralNode
  def doubleQuoteString: Parser[StringLiteralNode] =
    "\"".r ~> doubleQuoteStringContent <~ "\"".r

  def singleQuoteStringContent: Parser[StringLiteralNode] =
    """[^']*""".r  ^^ StringLiteralNode
  def singleQuoteString: Parser[StringLiteralNode] =
    "'".r ~> singleQuoteStringContent <~ "'".r

  def string: Parser[StringLiteralNode] = doubleQuoteString | singleQuoteString

  // FIXME string is too generic but discussion is required
  // to have a more specific AST node I think
  def typeLiteral: Parser[String] = namespacedIdentifier

  def literal: Parser[AST] = (
      floatNumber
    | intNumber
    | octIntNumber
    | hexIntNumber
    | binIntNumber
    | constTrue
    | constFalse
    | string
  )
}
