package org.moe.runtime.nativeobjects

import org.moe.runtime._
import scala.util.{Try, Success, Failure}

class MoeStrObject(
    v: String, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[String](v, klass) {

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

  // MoeNativeObject overrides

  override def copy = new MoeStrObject(getNativeValue, getAssociatedClass)

  override def isScalar: Boolean = true

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

  // coercing

  def toInt(r: MoeRuntime): MoeIntObject =
    r.NativeObjects.getInt(
      unboxToInt match {
        case Success(i) => i
        case Failure(e) => 0
      }
    )

  def toNum(r: MoeRuntime): MoeNumObject =
    r.NativeObjects.getNum(
      unboxToDouble match {
        case Success(d) => d
        case Failure(e) => 0.0
      }
    )

  def toBool(r: MoeRuntime): MoeBoolObject =
    r.NativeObjects.getBool(
      getNativeValue match {
        case ("")  => false
        case ("0") => false
        case _     => true
      }
    )

  def coerce(r: MoeRuntime, ctx: Option[MoeContext]): MoeObject = {
    ctx match {
      case Some(MoeIntContext()) => toInt(r)
      case Some(MoeNumContext()) => {
        import scala.util.matching.Regex
        val numPattern = new Regex("""[\-\+]?[0-9_]*\.[0-9_]+([eE][\-+]?[0-9_]+)?""")
        getNativeValue match {
          case numPattern(n) => toNum(r)
          case _             => toInt(r)
        }
      }
      case Some(MoeBoolContext()) => toBool(r)
      case None                  => this
      case _                     => throw new MoeErrors.CannotCoerceError(this.toString)
    }
  }
}
