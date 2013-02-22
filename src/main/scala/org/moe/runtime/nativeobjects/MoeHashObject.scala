package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.collection.mutable.HashMap
import scala.util.{Try, Success, Failure}

class MoeHashObject(
    v: HashMap[String, MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[HashMap[String, MoeObject]](v, klass) {

  private def hash = getNativeValue

  // Runtime methods

  def at_key(k: MoeStrObject): MoeObject = {
    hash.get(k.unboxToString.get).getOrElse(new MoeUndefObject())
  }

  def bind_key(k: MoeStrObject, v: MoeObject): MoeObject = {
    hash.put(k.unboxToString.get, v)
    v
  }

  def keys: MoeObject = new MoeArrayObject(hash.keys.map(s => new MoeStrObject(s)).toList)

  def values: MoeObject = new MoeArrayObject(hash.values.map(x => x).toList)

  def kv: MoeObject = new MoeArrayObject(
    hash.toList.map(
      p => new MoeArrayObject(List(new MoeStrObject(p._1), p._2))
    )
  )

  def pairs: MoeObject = new MoeArrayObject(hash.toList.map(p => new MoePairObject(p)))

  // MoeObject overrides 

  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'

  // unboxing
  
  override def unboxToMap: Try[HashMap[String, MoeObject]] = Success(getNativeValue)
}