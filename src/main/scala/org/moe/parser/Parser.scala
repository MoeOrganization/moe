package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._
 
object Parser extends RegexParsers {
  def intNumber   = """[1-9][0-9_]+""".r ^^ { n => IntLiteralNode(n.replace("_", "").toInt) }
  def octIntNumber = """0[0-9_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", ""), 8)) }
  def hexIntNumber = """0x[0-9A-Fa-f_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", "")
                                                                      .replace("0x", "").toUpperCase, 16)) }
  def binIntNumber = """0b[01_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", "").replace("0b", ""), 2)) }
  def floatNumber = """[0-9]*\.[0-9]+""".r ^^ { n => FloatLiteralNode(n.toDouble) }
  def constTrue  : Parser[AST] = "true" ^^ { _ => BooleanLiteralNode(true) }
  def constFalse : Parser[AST] = "false" ^^ { _ => BooleanLiteralNode(false) }

  def literal = floatNumber | intNumber | octIntNumber | hexIntNumber | binIntNumber | constTrue | constFalse

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(literal, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

