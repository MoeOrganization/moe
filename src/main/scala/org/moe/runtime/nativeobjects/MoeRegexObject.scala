package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.util.matching.Regex

class MoeRegexObject(
  rx: String,
  flags: Option[String] = None, 
  t: Option[MoeType] = None
  ) extends MoeNativeObject[(String, Option[String])]((rx, flags), t) {

  private val regex = new Regex(rx).unanchored

  def getRegex = regex
  def getFlags = flags.getOrElse("")

  def matches (r: MoeRuntime, text: MoeStrObject): MoeBoolObject = r.NativeObjects.getBool(
    regex.findFirstIn(text.unboxToString.get).nonEmpty
  )

  private def regexFlagsToMap(flags: String): Map[String, Boolean] = {
    val validFlags = Map(
      'i' -> "ignore_case",
      'g' -> "global",
      'm' -> "match_multiple_lines",
      's' -> "match_single_line",
      'x' -> "extended"
    )
    (for (f <- flags) yield (validFlags(f) -> true)).toMap
  }

  def find (r: MoeRuntime,
    target: MoeStrObject,
    flags: Option[MoeStrObject] = None
  ) = {
    val f: String = flags match {
      case Some(f) => f.unboxToString.get
      case None    => getFlags
    }

    // TODO: other flags
    val find_all = regexFlagsToMap(f).getOrElse("global", false)
    val matches = if (find_all)
      regex.findAllIn(target.unboxToString.get)
    else
      regex.findFirstIn(target.unboxToString.get)
  }

  def replace (
    r: MoeRuntime,
    target: MoeStrObject,
    replacement: MoeStrObject,
    flags: Option[MoeStrObject]
    ): MoeStrObject = {
    val f = flags match {
      case Some(f) => f.unboxToString.get
      case None    => getFlags
    }
    val replace_all = regexFlagsToMap(f).getOrElse("global", false)
    // TODO: other flags
    r.NativeObjects.getStr(
      if (replace_all)
        regex.replaceAllIn(target.unboxToString.get, replacement.unboxToString.get)
      else
        regex.replaceFirstIn(target.unboxToString.get, replacement.unboxToString.get)
    )
  }

  // MoeNativeObject overrides

  override def copy = new MoeRegexObject(getNativeValue._1, getNativeValue._2, getAssociatedType)

}
