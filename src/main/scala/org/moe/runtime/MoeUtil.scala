package org.moe.runtime

import scala.util.matching.Regex

object MoeUtil {

  // perlish modulo
  def perlModuloOp(a: Int, b: Int): Int = {
    val res = a % b
    if (a < 0)
      if (b > 0 && res < 0) res + b else res
    else
      if (b < 0) res + b else res
  }

  // strings matching the alphanumeric pattern 
  // /^[a-zA-Z]*[0-9]*\z/ are incrementable

  def magicalStringIncrement(str: String): String = {
    def incr_numeric_part(n: String) = {
      val len = n.length
      ("%0" + len + "d").format(n.toInt + 1).toString
    }

    def incr_alpha_part(s: String) = {

      def succ(c: Char, carry: Boolean): (Char, Boolean) = {
        if (carry)
          c match {
            case 'z' => ('a', true)
            case 'Z' => ('A', true)
            case _   => ((c.toInt + 1).toChar, false)
          }
        else
          (c, false)
      }

      var carry = true
      val incremented = (for (c <- s.reverse) yield {
        val succ_c = succ(c, carry)
        carry = succ_c._2
        succ_c._1
      }).reverse
      (if (carry) (incremented.head.toString ++ incremented) else incremented).mkString
    }

    def increment_in_parts(alpha: String, numeric: String) = {
      if (alpha.isEmpty) {
        // no alpha prefix; increment as numeric
        incr_numeric_part(numeric)
      } else if (numeric.isEmpty) {
        // only alpha part
        incr_alpha_part(alpha)
      } else {
        // both alpha and numeric parts exist. increment numeric
        // part first; if it carries over then increment alpha part
        val next_n = incr_numeric_part(numeric)
        if (next_n.length == numeric.length)
          alpha + next_n
        else
          incr_alpha_part(alpha) + next_n.tail
      }
    }

    val alpha_numeric = """^([a-zA-Z]*)([0-9]*)\z""".r
    str match {
      case alpha_numeric(alpha, numeric) => increment_in_parts(alpha, numeric)
      case _ => throw new MoeErrors.MoeException("string is not incrementable")
    }
  }
}