package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._

trait Base extends JavaTokenParsers {
  def identifier           = ident
  def namespaceSeparator   = "::"
  def namespacedIdentifier = repsep(identifier, namespaceSeparator) ^^ { _.mkString(namespaceSeparator) }
}
