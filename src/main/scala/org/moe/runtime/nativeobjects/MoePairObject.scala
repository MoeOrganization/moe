package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoePairObject(
    v: (String, MoeObject), 
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[(String, MoeObject)](v, klass) {

  // MoeObject overrides

  override def toString: String = getNativeValue match {
    case (k, v) => k + " => " + v.toString
  }

  // unboxing

  override def unboxToPair: Try[(String, MoeObject)] = Success(getNativeValue)
}