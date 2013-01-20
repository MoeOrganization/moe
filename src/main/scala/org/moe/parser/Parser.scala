package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._

import ParserUtils._

object Parser extends RegexParsers with Literals {

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

  // awwaiid's experimental structures

  def loop: Parser[AST] = ifLoop // | forLoop | foreachLoop | whileLoop

  def ifLoop: Parser[AST] =
    (("if" ~ "(") ~> expression) ~ (")" ~> block) ^^ { case a ~ b => IfNode(a,b) }

  // def forLoop = "for" ~ "(" ~> expression <~ ";" ~> expression <~ ";" ~> expression <~ ")" ~ block
  // def whileLoop = "if" ~ "(" ~> expression <~ ")" ~ block
  // def foreachLoop = "for(each)?".r ~ varDeclare ~ "(" ~> expression <~ ")" ~ block
  // def varDeclare = "my" ~ varName
  // def sigil = """[$@%]""".r
  // def bareName = """[a-zA-Z](\w*)""".r
  // def varName = sigil ~ bareName

  // def packageName = bareName
  // def className = bareName

  // FIXME I feel skipping over blank statements doesn't
  // portray the AST completely but I might just be splitting hairs
  def statementDelim: Parser[List[String]] = rep1(";")
  def statements: Parser[List[AST]] =
    repsep(statement, statementDelim)
  def blockContent: Parser[StatementsNode] =
    statements <~ statementDelim.? ^^ { StatementsNode(_) }
  def block: Parser[StatementsNode] =
    """\{""".r ~> blockContent <~ """\}""".r

  def doBlock: Parser[StatementsNode] = "do".r ~> block
  def scopeBlock: Parser[ScopeNode] = block ^^ { ScopeNode(_) }
  // def packageBlock = "package" ~> packageName ~ block
  // def classBlock = "class" ~> className ~ block

  def statement: Parser[AST] = (
      loop
    | expression
    | doBlock
    | scopeBlock
  )

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(statement, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

