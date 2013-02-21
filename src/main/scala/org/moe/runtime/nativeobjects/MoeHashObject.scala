package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoeHashObject(
    v: Map[String, MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Map[String, MoeObject]](v, klass) {

  // MoeObject overrides 

  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'

  // unboxing
  
  override def unboxToHash: Try[Map[String, MoeObject]] = Success(getNativeValue)
}