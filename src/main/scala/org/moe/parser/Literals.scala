package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Literals extends Base {

  // Numeric literals
  def zeroNumber: Parser[IntLiteralNode]   =
    """0""".r ^^ { n => IntLiteralNode(0) }
  def intNumber: Parser[IntLiteralNode]   =
    """-?[1-9][0-9_]*""".r ^^ { n => IntLiteralNode(formatInt(n)) }
  def octIntNumber: Parser[IntLiteralNode] =
    """0[0-9_]+""".r ^^ { n => IntLiteralNode(formatOctal(n)) }
  def hexIntNumber: Parser[IntLiteralNode] =
    """0x[0-9A-Fa-f_]+""".r ^^ { n => IntLiteralNode(formatHex(n)) }
  def binIntNumber: Parser[IntLiteralNode] =
    """0b[01_]+""".r ^^ { n => IntLiteralNode(formatBin(n)) }
  def floatNumber: Parser[FloatLiteralNode] =
    """-?[0-9_]*\.[0-9_]+([eE][\-+]?[0-9_]+)?""".r ^^ { n => FloatLiteralNode(formatFloat(n)) }

  // Boolean literals
  def constTrue: Parser[BooleanLiteralNode] =
    "true".r ^^^ BooleanLiteralNode(true)
  def constFalse: Parser[BooleanLiteralNode] =
    "false".r ^^^ BooleanLiteralNode(false)

  // Undef literal
  def constUndef: Parser[UndefLiteralNode] =
    "undef".r ^^^ UndefLiteralNode()

  // String literals

  // NOTE:
  // These two are basically duplicated except for
  // the start which disallows ' or " accordingly
  // this is pulled from the Scala JavaTokenParser
  // class and modified to be Perlish. It should be
  // noted that the unicode literal handling requires
  // 4 hex chars, whereas the Perl unicode docs have
  // variable widths. I don't know enough about unicode
  // to understand (or care) about the details, but
  // this should suffice (and perhaps even normalize
  // things a little)
  // - SL

  def doubleQuoteStringContents: Parser[StringLiteralNode] =
    """([^"\p{Cntrl}\\]|\\[\\'"bfnrt]|\\x\{[a-fA-F0-9]{4}\})*""".r ^^ StringLiteralNode
  def singleQuoteStringContents: Parser[StringLiteralNode] =
    """([^'\p{Cntrl}\\]|\\[\\'"bfnrt]|\\x\{[a-fA-F0-9]{4}\})*""".r ^^ StringLiteralNode

  def doubleQuoteString: Parser[StringLiteralNode] = "\"" ~> doubleQuoteStringContents <~ "\""
  def singleQuoteString: Parser[StringLiteralNode] = "'" ~> singleQuoteStringContents <~ "'"

  def string: Parser[StringLiteralNode] = doubleQuoteString | singleQuoteString

  // FIXME string is too generic but discussion is required
  // to have a more specific AST node I think
  def typeLiteral: Parser[String] = namespacedIdentifier

  def literalValue: Parser[AST] = (
      floatNumber
    | intNumber
    | octIntNumber
    | hexIntNumber
    | binIntNumber
    | zeroNumber
    | constTrue
    | constFalse
    | constUndef
    | string
  )
}
