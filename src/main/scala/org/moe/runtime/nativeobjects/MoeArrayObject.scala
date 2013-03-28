package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

class MoeArrayObject(
    v: ArrayBuffer[MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[ArrayBuffer[MoeObject]](v, klass) {

  def this(list:List[MoeObject]) = this(ArrayBuffer(list : _*));

  private def array = getNativeValue

  // Runtime methods
  
  def at_pos (r: MoeRuntime, i: MoeIntObject): MoeObject = {
    if(i.unboxToInt.get >= array.length) r.NativeObjects.getUndef
    else array(i.unboxToInt.get)
  }

  def bind_pos (r: MoeRuntime, i: MoeIntObject, v: MoeObject): MoeObject = {
    val idx = i.unboxToInt.get
    if (idx >= array.length) setNativeValue(array.padTo(idx, r.NativeObjects.getUndef))
    array.insert(idx, v)
    v
  }

  def length (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(array.length)

  def clear (r: MoeRuntime): MoeObject = {
    array.clear()
    r.NativeObjects.getUndef
  }

  def shift (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(0)

  def pop (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(array.length - 1)

  def unshift (r: MoeRuntime, values: MoeArrayObject): MoeIntObject = {
    array.insertAll(0, values.unboxToArrayBuffer.get)
    r.NativeObjects.getInt(array.length)
  }

  def push (r: MoeRuntime, values: MoeArrayObject): MoeIntObject = {
    array ++= values.unboxToArrayBuffer.get
    r.NativeObjects.getInt(array.length)
  }

  def slice(r: MoeRuntime, indicies: MoeArrayObject): MoeArrayObject = r.NativeObjects.getArray(
    indicies.unboxToArrayBuffer.get.map(i => at_pos(r, i.asInstanceOf[MoeIntObject])) : _*
  )

  def reverse(r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(array.reverse:_*)

  def join(r: MoeRuntime): MoeStrObject = r.NativeObjects.getStr(array.map(_.toString).mkString(""))
  def join(r: MoeRuntime, sep: MoeStrObject): MoeStrObject = r.NativeObjects.getStr(
    array.map(_.unboxToString.get).mkString(sep.unboxToString.get)
  )

  // MoeObject overrides
  
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '[' + getNativeValue.map(_.toString).mkString(", ") + ']'
  
  // unboxing
  
  override def unboxToString: Try[String] = Success(toString)
  override def unboxToArrayBuffer: Try[ArrayBuffer[MoeObject]] = Success(getNativeValue)
}
