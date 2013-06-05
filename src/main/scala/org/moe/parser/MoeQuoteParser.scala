package org.moe.parser

import scala.util.parsing.combinator._
import scala.util.matching.Regex

trait MoeQuoteParser extends JavaTokenParsers {
  val quotePairMap = Map(
    '(' -> ')',
    '[' -> ']',
    '{' -> '}',
    '<' -> '>'
  )

  private def uptoEndDelim(in: Input, beginDelim: Char, endDelim: Char) = {
    val source = in.source
    val offset = in.offset
    var j = offset + 1
    // beginDelim found; copy input upto endDelim
    val buf = new StringBuilder
    val pairedDelim = beginDelim != endDelim

    var nestingLevel = 0;
    var done = false;
    while (j < source.length && !done) {
      source.charAt(j) match {
        case '\\' if source.charAt(j+1) == beginDelim || source.charAt(j+1) == endDelim=> {
          j += 1
          buf += source.charAt(j)
        }
        case `beginDelim` => {
          if (pairedDelim) {
            nestingLevel += 1
            buf += source.charAt(j)
          }
          else {
            done = true
          }
        }
        case `endDelim` => {
          if (nestingLevel == 0) {
            done = true
          }
          else {
            nestingLevel -= 1
            buf += source.charAt(j)
          }
        }
        case _ => {
          buf += source.charAt(j)
        }
      }
      j += 1
    }

    if (done) {
      (Some(buf.toString), in.drop(j - offset - 1))
    }
    else {
      (None, in.drop(j - offset - 1))
    }
  }

  def quoted(q: Char): Parser[String] = new Parser[String] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)
      // println(s"source = |$source|, offset = |$offset|, start = |$start|")

      var j = start
      val beginDelim = q
      val endDelim = quotePairMap.getOrElse(beginDelim, beginDelim)

      if (j < source.length && beginDelim != source.charAt(j)) {
        Failure("`" + q + "' expected but not found", in.drop(start - offset))
      }
      else {
        uptoEndDelim(in.drop(start - offset), beginDelim, endDelim) match {
          case (Some(matched), rest_in) => Success(matched, rest_in.drop(1))    // drop endDelim from input
          case (None,          rest_in) => Failure("`" + endDelim + "' expected but not found.", rest_in)
        }
      }
    }
  }

  def quoted(q: Regex): Parser[String] = new Parser[String] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)

      (q findPrefixMatchOf(source.subSequence(start, source.length))) match {
        case Some(matched) => {
          val beginDelim = source.subSequence(start, start + matched.end).charAt(0)
          val endDelim = quotePairMap.getOrElse(beginDelim, beginDelim)
          uptoEndDelim(in.drop(start - offset), beginDelim, endDelim) match {
            case (Some(matched), rest_in) => Success(matched, rest_in.drop(1))    // drop endDelim from input
            case (None,          rest_in) => Failure("`" + endDelim + "' expected but not found.", rest_in)
          }
        }
        case None =>
          Failure("`string matching " + q + "' expected but not found", in.drop(start - offset))
      }
    }
  }

  def quotedPair(q: Char): Parser[(String, String)] = new Parser[(String, String)] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)
      // println(s"source = |$source|, offset = |$offset|, start = |$start|")

      var j = start
      val beginDelim = q
      val endDelim = quotePairMap.getOrElse(beginDelim, beginDelim)

      if (j < source.length && beginDelim != source.charAt(j)) {
        Failure("`" + q + "' expected but not found", in.drop(start - offset))
      }
      else {
        uptoEndDelim(in.drop(start - offset), beginDelim, endDelim) match {
          case (Some(matched_1), rest_in) => {
            uptoEndDelim(rest_in, beginDelim, endDelim) match {
              case (Some(matched_2), rest_in_2) => Success((matched_1, matched_2), rest_in_2.drop(1))    // drop endDelim from input
              case (None,            rest_in_2) => Failure("`" + endDelim + "' expected but not found.", rest_in_2)
            }
          }
          case (None,          rest_in) => Failure("`" + endDelim + "' expected but not found.", rest_in)
        }
      }
    }
  }
}
