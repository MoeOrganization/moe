package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._
 
object Parser extends RegexParsers {
  def intNumber   = """[0-9]+""".r ^^ { n => IntLiteralNode(n.toInt) }
  def floatNumber = """[0-9]+(\.[0-9]+)?""".r ^^ { n => FloatLiteralNode(n.toDouble) }
  def constTrue  : Parser[AST] = "true" ^^ { _ => BooleanLiteralNode(true) }
  def constFalse : Parser[AST] = "false" ^^ { _ => BooleanLiteralNode(false) }

  def literal = intNumber | floatNumber | constTrue | constFalse

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(literal, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

