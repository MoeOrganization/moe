package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.collection.mutable.HashMap
import scala.util.{Try, Success, Failure}

class MoeHashObject(
    v: HashMap[String, MoeObject],
    t : Option[MoeType] = None
  ) extends MoeNativeObject[HashMap[String, MoeObject]](v, t) {

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
    keys.unboxToArrayBuffer.get.map(k => at_key(r, k.asInstanceOf[MoeStrObject])):_*
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
    hash.toList.map({
      case (k, v) => r.NativeObjects.getArray(r.NativeObjects.getStr(k), v)
    }).toArray:_*
  )

  // equality
  def equal_to (r: MoeRuntime, that: MoeHashObject): MoeBoolObject = {
    val k1 = hash.keys.toList.sortWith(_ < _)
    val k2 = that.getNativeValue.keys.toList.sortWith(_ < _)
    r.NativeObjects.getBool(
      k1.length == k2.length
        &&
      k1 == k2
        &&
      ((k1.map(k => hash(k)), k1.map(k => that.getNativeValue(k))).zipped.forall( (a, b) => a.equal_to(b) ))
    )
  }

  // MoeNativeObject overrides
 
  override def copy = new MoeHashObject(HashMap(getNativeValue.toSeq:_*), getAssociatedType)

  // MoeObject overrides 

  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'

  // unboxing
  
  override def unboxToMap: Try[HashMap[String, MoeObject]] = Success(getNativeValue)
}
