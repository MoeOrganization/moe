package org.moe.runtime.nativeobjects

import org.moe.runtime._
import scala.util.{Try, Success, Failure}

class MoeStrObject(
    v: String, t : Option[MoeType] = None
  ) extends MoeNativeObject[String](v, t) {

  // runtime methods

  def increment (r: MoeRuntime): Unit = setNativeValue(MoeUtil.magicalStringIncrement(getNativeValue))

  def chomp (r: MoeRuntime): MoeBoolObject = {
    val s = getNativeValue
    if (s.indexOf("\n") == s.length - 1) {
      setNativeValue(s.dropRight(1))
      return r.NativeObjects.getTrue
    } else {
      return r.NativeObjects.getFalse
    }
  }

  def chop (r: MoeRuntime): MoeStrObject = {
    val s = getNativeValue
    val (n, c) = s.splitAt(s.length - 1)
    setNativeValue(n)
    r.NativeObjects.getStr(c)
  }

  def uc (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue.toUpperCase())
  def lc (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue.toLowerCase())

  def ucfirst (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue.capitalize)
  def lcfirst (r: MoeRuntime): MoeStrObject = {
    val s = getNativeValue
    val (n, c) = s.splitAt(1)
    r.NativeObjects.getStr(n.toLowerCase() + c)    
  }

  def length (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(getNativeValue.length)
  def reverse (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue.reverse)

  def split (r: MoeRuntime, s: MoeStrObject): MoeArrayObject = r.NativeObjects.getArray(
    getNativeValue.split(s.unboxToString.get).map(r.NativeObjects.getStr(_)).toArray:_* 
  ) 

  def concat (r: MoeRuntime, x: MoeStrObject): MoeStrObject = r.NativeObjects.getStr(
    getNativeValue + x.unboxToString.get
  )

  def concatAll (r: MoeRuntime, a: MoeArrayObject): MoeStrObject = r.NativeObjects.getStr(
    getNativeValue + a.unboxToArrayBuffer.get.map(_.unboxToString.get).mkString
  )

  def pad (r: MoeRuntime, n: MoeIntObject): MoeStrObject = r.NativeObjects.getStr(
    List.fill(n.unboxToInt.get)(" ").mkString + getNativeValue
  )

  def rpad (r: MoeRuntime, n: MoeIntObject): MoeStrObject = r.NativeObjects.getStr(
    getNativeValue + List.fill(n.unboxToInt.get)(" ").mkString
  )

  def index (r: MoeRuntime): Unit = {}  //(r: MoeRuntime): Unit = {} // ($substring, ?$position)
  def rindex (r: MoeRuntime): Unit = {} // ($substring, ?$position)
  def sprintf (r: MoeRuntime): Unit = {} // ($format, @items)
  def substr (r: MoeRuntime): Unit = {} // ($offset, ?$length)
  def quotemeta (r: MoeRuntime): Unit = {} 

  def repeat (r: MoeRuntime, other: MoeIntObject): MoeStrObject = {
    val str = getNativeValue
    val n   = other.unboxToInt.get
    r.NativeObjects.getStr(List.fill(n)(str).mkString)
  }

  // equality

  def equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue == other.unboxToString.get)

  def not_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue != other.unboxToString.get)

  def compare_to (r: MoeRuntime, other: MoeObject): MoeIntObject =
    r.NativeObjects.getInt(
      getNativeValue compareTo other.unboxToString.get match {
        case 0 => 0
        case r => if (r < 0) -1 else 1
      }
    )

  // comparison

  def less_than (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue < other.unboxToString.get)

  def greater_than (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue > other.unboxToString.get)

  def less_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue <= other.unboxToString.get)

  def greater_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject =
    r.NativeObjects.getBool(getNativeValue >= other.unboxToString.get)

  // regular expression matching

  def matches (r: MoeRuntime, pattern: MoeStrObject): MoeBoolObject =
    new MoeRegexObject(pattern.unboxToString.get).matches(r, this)

  def matches (r: MoeRuntime, pattern: MoeRegexObject): MoeBoolObject =
    pattern.matches(r, this)

  // TODO: find method that returns a "Match" object to access captures etc

  def subst (
    r: MoeRuntime,
    pattern: MoeStrObject,
    replacement: MoeStrObject,
    flags: MoeStrObject
  ): MoeStrObject =
    r.NativeObjects.getStr(
      if (flags.unboxToString.get == "g")
        getNativeValue.replace(pattern.unboxToString.get, replacement.unboxToString.get)
      else
        getNativeValue.replaceFirst(pattern.unboxToString.get, replacement.unboxToString.get)
    )

  def subst (
    r: MoeRuntime,
    pattern: MoeRegexObject,
    replacement: MoeStrObject,
    flags: MoeStrObject
  ): MoeStrObject =
    pattern.replace(r, this, replacement, Some(flags))

  // transliteration -- like in Perl5, except /r flag is the default
  // behavior; i.e. the original string is not modified and the
  // transliterated string is returned

  import scala.util.matching.Regex._
  def trans(
    r: MoeRuntime,
    search: MoeStrObject,
    replace: MoeStrObject,
    flags: MoeStrObject
  ): MoeStrObject = {
    def expandCharSequence(s: String): List[Char] = {
      s.foldLeft(List[Char]()){
        (a, c) => if (a.length > 1 && a.last == '-') a.dropRight(2) ++ (a.init.last to c).toList else a ++ List(c)
      }
    }

    val complement = flags.unboxToString.get.contains('c')
    val squash     = flags.unboxToString.get.contains('s')
    val delete     = flags.unboxToString.get.contains('d')

    val searchList = expandCharSequence(search.unboxToString.get)
    var replaceList_t = expandCharSequence(replace.unboxToString.get)

    val replaceList = if (delete) {
                          replaceList_t // use the replace-list as is
                      }
                      else {
                        if (replaceList_t.isEmpty)
                          searchList
                        else    // truncate/extend replace-list to match search-list length
                          if (replaceList_t.length > searchList.length)
                            replaceList_t.drop(replaceList_t.length - searchList.length)
                          else if (searchList.length > replaceList_t.length)
                            replaceList_t ++ List.fill(searchList.length - replaceList_t.length)(replaceList_t.last)
                          else
                            replaceList_t
                      }

    val transMap = searchList.zip(replaceList).toMap

    def isFound(c: Char) = if (complement) !searchList.contains(c) else searchList.contains(c)
    def maybeSquashed(a: String, c: Char) = if (squash && !a.isEmpty && a.last == c) a else a + c

    r.NativeObjects.getStr(
      getNativeValue.foldLeft(""){
        (a, c) => {
          if (isFound(c)) {
            if (complement)
              if (delete) a else maybeSquashed(a, replaceList.last)
            else
              transMap.get(c) match {
                case Some(x) => maybeSquashed(a, x)
                case None    => if (delete) a else a + c
              }
          }
          else {
            a + c
          }
        }
      }
    )
  }

  // MoeNativeObject overrides

  override def copy = new MoeStrObject(getNativeValue, getAssociatedType)

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue match {
    case "" | "0" => true
    case _        => false
  }
  override def toString = "\"" + getNativeValue + "\""

  // unboxing

  override def unboxToString : Try[String] = Try(getNativeValue)
  override def unboxToInt    : Try[Int]    = Try(getNativeValue.toInt)
  override def unboxToDouble : Try[Double] = Try(getNativeValue.toDouble)

}
