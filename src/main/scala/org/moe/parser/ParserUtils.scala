package org.moe.parser

object ParserUtils {

  def normPerlInt (s: String): String = s.replace("_", "")
  def formatInt   (s: String): Int    = normPerlInt(s.replace("+", "")).toInt
  def formatFloat (s: String): Double = normPerlInt(s).toDouble
  def formatOctal (s: String): Int    = Integer.parseInt( normPerlInt(s), 8 )
  def formatHex   (s: String): Int    = Integer.parseInt( normPerlInt(s).substring(2).toUpperCase, 16 )
  def formatBin   (s: String): Int    = Integer.parseInt( normPerlInt(s).substring(2).toUpperCase, 2  )

  // handle escaped characters in double-quoted strings
  def formatStr   (s: String): String = {
    val escaped = Map(
      "\\b" -> "\b",
      "\\f" -> "\f",
      "\\n" -> "\n",
      "\\r" -> "\r",
      "\\t" -> "\t",
      "\\$" -> """\$"""
    )
    val escapes = """\\([bfnrt'"\{\}\(\)\[\]\&\$\\])""".r
    escapes.replaceAllIn(s, m => escaped.getOrElse(m.group(0), m.group(0).tail))
  }

}
