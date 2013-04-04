package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

/**
 * NOTE:
 * this is the one outlyer, it doesn't need to be
 * a NativeObject subclass because it doesn't need 
 * to actually box any values, just the lack of a 
 * value
 */

class MoeUndefObject(
    klass : Option[MoeClass] = None
  ) extends MoeObject(klass) {

  // MoeNativeObject overrides

  def getNativeValue: AnyRef = null
  
  def copy = new MoeUndefObject(getAssociatedClass)

  // MoeObject overrides

  override def isFalse: Boolean = true
  override def isUndef: Boolean = true
  override def toString: String = "undef"
}
