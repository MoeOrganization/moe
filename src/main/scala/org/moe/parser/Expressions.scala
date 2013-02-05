package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Expressions extends Literals with JavaTokenParsers {

  private lazy val array_index_rule = sigil ~
                                      (namespacedIdentifier <~ "[") ~
                                      (expression <~ "]")

  def expression: Parser[AST] = (
      arrayIndex
    | hash
    | array
    | literalValue
    | arrayRef
    | hashRef
    | declaration
    | variable
    | expressionParens
  )

  def expressionParens: Parser[AST] = "(" ~> expression <~ ")"

  // List stuff
  def list: Parser[List[AST]] = (literal(",").? ~> repsep(expression, ",") <~ literal(",").?)
  def array: Parser[ArrayLiteralNode] = "(" ~> list <~ ")" ^^ ArrayLiteralNode
  def arrayRef: Parser[ArrayRefLiteralNode] =
    "[" ~> list <~ "]" ^^ ArrayRefLiteralNode

  // Hash stuff
  def barehashKey: Parser[StringLiteralNode] =
    """[0-9\w_]*""".r ^^ StringLiteralNode
  def hashKey: Parser[AST] = scalar | barehashKey
  def pair: Parser[PairLiteralNode] =
    (hashKey <~ "=>") ~ expression ^^ { case k ~ v => PairLiteralNode(k, v) }
  def hashContent: Parser[List[PairLiteralNode]] =
    repsep(pair, ",")
  def hash: Parser[HashLiteralNode] =
    "(" ~> hashContent <~ ")" ^^ HashLiteralNode
  def hashRef: Parser[HashRefLiteralNode] =
    "{" ~> hashContent <~ "}" ^^ HashRefLiteralNode

  // Variable stuff
  def sigil = """[$@%]""".r
  def varname = sigil ~ namespacedIdentifier ^^ { case a ~ b => a + b }
  def variable = varname ^^ VariableAccessNode

  def simpleScalar = "$" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("$" + i) }
  def simpleArray  = "@" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("@" + i) }
  def simpleHash   = "%" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("%" + i) }

  def declaration = "my" ~> varname ~ ("=" ~> expression).? ^^ {
    case v ~ expr => VariableDeclarationNode(v, expr.getOrElse(UndefLiteralNode()))
  }

  def arrayIndex = array_index_rule ^^ {
    case "$" ~ i ~ expr => ArrayElementAccessNode("@" + i, expr)
  }

  def scalar: Parser[AST] = simpleScalar | arrayIndex | literalValue

}
