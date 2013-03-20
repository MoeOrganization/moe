package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.util.{Try, Success, Failure}

class MoeBoolObject(
    v: Boolean, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Boolean](v, klass) {

  // runtime methods

  def toInt: Int = if (getNativeValue) 1 else 0

  override def isFalse: Boolean = getNativeValue == false
  override def toString: String = if (getNativeValue) "true" else "false"

  // unboxing

  override def unboxToInt: Try[Int] = Try(toInt)
}
