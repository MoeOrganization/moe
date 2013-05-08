package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait MoeLiterals extends JavaTokenParsers {

  // treat comments as whitespace
  override val whiteSpace = """(#[^\n\r]*[\n\r]|\s)+""".r

  // Numeric literals
  def zeroNumber: Parser[IntLiteralNode]   =
    """[\-\+]?0""".r ^^ { n => IntLiteralNode(0) }
  def intNumber: Parser[IntLiteralNode]   =
    """[\-\+]?[1-9][0-9_]*""".r ^^ { n => IntLiteralNode(formatInt(n)) }
  def octIntNumber: Parser[IntLiteralNode] =
    """0[0-9_]+""".r ^^ { n => IntLiteralNode(formatOctal(n)) }
  def hexIntNumber: Parser[IntLiteralNode] =
    """0x[0-9A-Fa-f_]+""".r ^^ { n => IntLiteralNode(formatHex(n)) }
  def binIntNumber: Parser[IntLiteralNode] =
    """0b[01_]+""".r ^^ { n => IntLiteralNode(formatBin(n)) }
  def floatNumber: Parser[FloatLiteralNode] =
    """[\-\+]?[0-9_]*\.[0-9_]+([eE][\-+]?[0-9_]+)?""".r ^^ { n => FloatLiteralNode(formatFloat(n)) }

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

  val doubleQuoteStringPattern = """"((?:[^"\p{Cntrl}\\]|\\[\\'"bfnrt]|\\x\{[a-fA-F0-9]{4}\})*)"""".r
  def doubleQuoteString: Parser[StringLiteralNode] = doubleQuoteStringPattern ^^ {
    case doubleQuoteStringPattern(s) => StringLiteralNode(formatStr(s))
 }

  val singleQuoteStringPattern = """'((?:[^'\p{Cntrl}\\]|\\[\\'"bfnrt]|\\x\{[a-fA-F0-9]{4}\})*)'""".r
  def singleQuoteString: Parser[StringLiteralNode] = singleQuoteStringPattern ^^ {
    case singleQuoteStringPattern(s) => StringLiteralNode(s)
  }

  def string: Parser[StringLiteralNode] = doubleQuoteString | singleQuoteString

  // Self Literal

  def selfLiteral  : Parser[SelfLiteralNode]  = "self".r ^^^ SelfLiteralNode()
  def superLiteral : Parser[SuperCallNode] = "super".r ^^^ SuperCallNode()

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
    | selfLiteral
    | superLiteral
  )
}
