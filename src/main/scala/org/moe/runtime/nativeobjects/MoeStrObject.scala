package org.moe.runtime.nativeobjects

import org.moe.runtime._
import scala.collection.mutable.ArrayBuffer
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

  def concat (r: MoeRuntime, x: MoeObject): MoeStrObject = x match {
    case s: MoeStrObject   => r.NativeObjects.getStr( getNativeValue + s.unboxToString.get )
    case a: MoeArrayObject => r.NativeObjects.getStr( 
      getNativeValue + a.unboxToList.get.map(_.unboxToString.get).mkString
    )
  }

  def index (r: MoeRuntime): Unit = {}  //(r: MoeRuntime): Unit = {} // ($substring, ?$position)
  def rindex (r: MoeRuntime): Unit = {} // ($substring, ?$position)
  def sprintf (r: MoeRuntime): Unit = {} // ($format, @items)
  def substr (r: MoeRuntime): Unit = {} // ($offset, ?$length)
  def quotemeta (r: MoeRuntime): Unit = {} 

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue match {
    case "" | "0" => true
    case _        => false
  }
  override def toString = "\"" + getNativeValue + "\""
  
  // unboxing
  
  override def unboxToString: Try[String] = Success(getNativeValue)
  override def unboxToInt: Try[Int] = Try(getNativeValue.toInt)
  override def unboxToDouble: Try[Double] = Try(getNativeValue.toDouble)

}
