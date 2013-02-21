package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoeBoolObject(
    v: Boolean, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Boolean](v, klass) {

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue == false
  override def toString: String = if (getNativeValue) "true" else "false"
}