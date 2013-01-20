package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._

trait Base extends RegexParsers {
  def identifier = """[a-zA-Z_][\w_]*""".r

  def namespaceSeparator = "::"
  def namespacedIdentifier = repsep(identifier, namespaceSeparator) ^^ { _.mkString(namespaceSeparator) }
}
