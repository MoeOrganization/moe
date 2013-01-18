package org.moe.parser

import scala.util.parsing.combinator._
// import scala.util.matching.Regex
import org.moe.ast._

object Parser extends RegexParsers {

  // Numeric literals
  def intNumber   = """[1-9][0-9_]*""".r ^^ { n => IntLiteralNode(n.replace("_", "").toInt) }
  def octIntNumber = """0[0-9_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", ""), 8)) }
  def hexIntNumber = """0x[0-9A-Fa-f_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", "")
                                                                      .replace("0x", "").toUpperCase, 16)) }
  def binIntNumber = """0b[01_]+""".r ^^ { n => IntLiteralNode(Integer.parseInt(n.replace("_", "").replace("0b", ""), 2)) }
  def floatNumber = """[0-9_]*\.[0-9_]+""".r ^^ { n => FloatLiteralNode(n.replace("_", "").toDouble) }

  // Boolean literals
  def constTrue = "true".r ^^^ BooleanLiteralNode(true)
  def constFalse = "false".r ^^^ BooleanLiteralNode(false)

  // String literals
  def doubleQuoteStringContent = """[^"]*""".r ^^ StringLiteralNode
  def doubleQuoteString = "\"".r ~> doubleQuoteStringContent <~ "\"".r

  def singleQuoteStringContent = """[^']*""".r  ^^ StringLiteralNode
  def singleQuoteString = "'".r ~> singleQuoteStringContent <~ "'".r

  def string = doubleQuoteString | singleQuoteString

  def literal = floatNumber | intNumber | octIntNumber | hexIntNumber | binIntNumber | constTrue | constFalse | string

  def expression: Parser[AST] = literal | arrayRef

  // List stuff
  def list = (",?".r ~> repsep(expression, ",") <~ ",?".r)

  def arrayRef = "[" ~> list <~ "]" ^^ ArrayLiteralNode



  // awwaiid's experimental structures

  def loop: Parser[AST] = ifLoop // | forLoop | foreachLoop | whileLoop 

  def ifLoop: Parser[AST] = (("if" ~ "(") ~> expression) ~ (")" ~> block) ^^ { case a ~ b => IfNode(a,b) }

  // def forLoop = "for" ~ "(" ~> expression <~ ";" ~> expression <~ ";" ~> expression <~ ")" ~ block
  // def whileLoop = "if" ~ "(" ~> expression <~ ")" ~ block
  // def foreachLoop = "for(each)?".r ~ varDeclare ~ "(" ~> expression <~ ")" ~ block
  // def varDeclare = "my" ~ varName
  // def sigil = """[$@%]""".r
  // def bareName = """[a-zA-Z](\w*)""".r
  // def varName = sigil ~ bareName

  // def packageName = bareName
  // def className = bareName

  def statement = loop | expression
  def blockContent = repsep(statement, ";") <~ ";?".r ^^ { StatementsNode(_) }
  def block = "{" ~> blockContent <~ "}"
  // def packageBlock = "package" ~> packageName ~ block
  // def classBlock = "class" ~> className ~ block

  // Parser wrapper -- indicates the start node
  def parseStuff(input: String): AST = parseAll(statement, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
 

