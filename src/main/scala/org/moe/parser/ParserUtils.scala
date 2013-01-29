package org.moe.parser

object ParserUtils {
  def normPerlInt(s: String): String = {
    s.replace("_", "")
  }

  def formatInt(s: String): Int = {
    normPerlInt(s).toInt
  }

  def formatFloat(s: String): Double = {
    normPerlInt(s).toDouble
  }

  def formatOctal(s: String): Int = {
    Integer.parseInt(normPerlInt(s), 8)
  }

  def formatHex(n: String): Int = {
    Integer.parseInt(normPerlInt(n).substring(2).toUpperCase, 16)
  }

  def formatBin(n: String): Int = {
    Integer.parseInt(normPerlInt(n).substring(2).toUpperCase, 2)
  }
}
