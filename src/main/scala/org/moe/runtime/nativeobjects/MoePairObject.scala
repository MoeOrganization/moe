package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoePairObject(
    v: (String, MoeObject), 
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[(String, MoeObject)](v, klass) {

  def key   (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue._1)
  def value (r: MoeRuntime): MoeObject    = getNativeValue._2

  def kv (r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(
    r.NativeObjects.getStr(getNativeValue._1), getNativeValue._2
  )

  // MoeObject overrides

  override def toString: String = getNativeValue match {
    case (k, v) => k + " => " + v.toString
  }

  // unboxing

  override def unboxToTuple: Try[(String, MoeObject)] = Success(getNativeValue)
}