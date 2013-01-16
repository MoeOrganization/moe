package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._
 
object Parser extends RegexParsers {
  def intNumber   = """[0-9]+""".r ^^ { n => IntLiteralNode(n.toInt) }
  def floatNumber = """[0-9]+(\.[0-9]+)?""".r ^^ { n => FloatLiteralNode(n.toDouble) }

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(intNumber, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

