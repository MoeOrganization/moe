package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.collection.mutable.HashMap
import scala.util.{Try, Success, Failure}

class MoeHashObject(
    v: HashMap[String, MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[HashMap[String, MoeObject]](v, klass) {

  // MoeObject overrides 

  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'

  // unboxing
  
  override def unboxToMap: Try[HashMap[String, MoeObject]] = Success(getNativeValue)
}