package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Expressions extends Literals with JavaTokenParsers {

  private lazy val array_index_rule = sigil ~
                                      (namespacedIdentifier <~ literal("[")) ~
                                      (expression <~ literal("]"))

  def expression: Parser[AST] = (
      arrayIndex
    | array
    | literal
    | arrayRef
    | hashRef
    | declaration
    | variable
    | expressionParens
  )

  def expressionParens: Parser[AST] = literal("(") ~> expression <~ literal(")")

  // List stuff
  def list: Parser[List[AST]] = (",?".r ~> repsep(expression, ",") <~ ",?".r)
  def array: Parser[ArrayLiteralNode] = literal("(") ~> list <~ literal(")") ^^ ArrayLiteralNode
  def arrayRef: Parser[ArrayRefLiteralNode] =
    literal("[") ~> list <~ literal("]") ^^ ArrayRefLiteralNode

  // Hash stuff
  def barehashKey: Parser[StringLiteralNode] =
    """[0-9\w_]*""".r ^^ StringLiteralNode
  def hashKey: Parser[StringLiteralNode] = barehashKey | string
  // FIXME should be able to do <~ "=>".r ~> but couldn't fiddle with it enough
  def pair: Parser[PairLiteralNode] =
    hashKey ~ "=>".r ~ expression ^^ { case a ~ b ~ c => PairLiteralNode(a, c) }
  def hashContent: Parser[List[PairLiteralNode]] =
    repsep(pair, ",")
  def hashRef: Parser[HashLiteralNode] =
    literal("{") ~> hashContent <~ literal("}") ^^ HashLiteralNode

  // Variable stuff
  def sigil = """[$@%]""".r
  def varname = sigil ~ namespacedIdentifier ^^ { case a ~ b => a + b }
  def variable = varname ^^ VariableAccessNode

  def declaration = "my".r ~> varname ~ ("=".r ~> expression).? ^^ {
    case v ~ expr => VariableDeclarationNode(v, expr.getOrElse(UndefLiteralNode()))
  }

  def arrayIndex = array_index_rule ^^ {
    case "$" ~ i ~ expr => ArrayElementAccessNode("@" + i, expr)
  }

}
