package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoeStrObject(
    v: String, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[String](v, klass) {

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