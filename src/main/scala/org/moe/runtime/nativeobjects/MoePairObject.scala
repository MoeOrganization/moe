package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoePairObject(
    v: (String, MoeObject), 
    t : Option[MoeType] = None
  ) extends MoeNativeObject[(String, MoeObject)](v, t) {

  // runtime methods

  def key   (r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(getNativeValue._1)
  def value (r: MoeRuntime): MoeObject    = getNativeValue._2

  def kv (r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(
    r.NativeObjects.getStr(getNativeValue._1), getNativeValue._2
  )

  // MoeNativeObject overrides

  override def copy = new MoePairObject(getNativeValue._1 -> getNativeValue._2, getAssociatedType)

  // MoeObject overrides

  override def toString: String = getNativeValue match {
    case (k, v) => k + " => " + v.toString
  }

  // unboxing

  override def unboxToTuple: Try[(String, MoeObject)] = Success(getNativeValue)
}