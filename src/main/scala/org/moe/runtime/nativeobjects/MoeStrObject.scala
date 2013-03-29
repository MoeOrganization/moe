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
