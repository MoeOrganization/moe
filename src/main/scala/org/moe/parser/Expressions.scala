package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Expressions extends Literals {

  def expression: Parser[AST] = (
      literal
    | arrayRef
    | hashRef
  )

  // List stuff
  def list: Parser[List[AST]] = (",?".r ~> repsep(expression, ",") <~ ",?".r)

  def arrayRef: Parser[ArrayLiteralNode] =
    """\[""".r ~> list <~ """\]""".r ^^ ArrayLiteralNode

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
    """\{""".r ~> hashContent <~ """\}""".r ^^ HashLiteralNode

}
