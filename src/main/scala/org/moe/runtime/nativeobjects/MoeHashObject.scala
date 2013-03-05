package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

class MoeHashObject(
    v: HashMap[String, MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[HashMap[String, MoeObject]](v, klass) {

  private def hash = getNativeValue

  // Runtime methods

  def at_key(r: MoeRuntime, k: MoeStrObject): MoeObject = {
    hash.get(k.unboxToString.get).getOrElse(r.NativeObjects.getUndef)
  }

  def bind_key(r: MoeRuntime, k: MoeStrObject, v: MoeObject): MoeObject = {
    hash.put(k.unboxToString.get, v)
    v
  }

  def exists(r: MoeRuntime, k: MoeStrObject): MoeBoolObject = {
    if (hash.contains(k.unboxToString.get)) r.NativeObjects.getTrue else r.NativeObjects.getFalse
  }

  def slice(r: MoeRuntime, keys: MoeArrayObject) = r.NativeObjects.getArray(
    keys.unboxToList.get.map(k => at_key(r, k.asInstanceOf[MoeStrObject])):_*
  )

  def clear(r: MoeRuntime) = { 
    hash.clear()
    r.NativeObjects.getUndef
  }

  def keys(r: MoeRuntime) = r.NativeObjects.getArray(
    hash.keys.map(s => r.NativeObjects.getStr(s)).toArray:_*
  )

  def values(r: MoeRuntime) = r.NativeObjects.getArray(
    hash.values.map(x => x).toArray:_*
  )

  def pairs(r: MoeRuntime) = r.NativeObjects.getArray(
    hash.toList.map(p => r.NativeObjects.getPair(p)).toArray:_*
  )

  def kv(r: MoeRuntime) = r.NativeObjects.getArray(
    hash.toList.map(
      p => r.NativeObjects.getArray(r.NativeObjects.getStr(p._1), p._2)
    ).toArray:_*
  )

  // MoeObject overrides 

  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'

  // unboxing
  
  override def unboxToMap: Try[HashMap[String, MoeObject]] = Success(getNativeValue)
}
