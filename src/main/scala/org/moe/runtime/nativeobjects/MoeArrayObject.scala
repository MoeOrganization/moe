package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

// NOTE:
// List[A] is a poor internal representation
// for a MoeArray, this will need to be thought
// through much more. It looks like ListBuffer
// could be suitable, but we might need to just
// write our own in the long run.
// - SL

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

  def length (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(array.length)

  def clear (r: MoeRuntime) = array.clear

  def shift (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(0)

  def pop (r: MoeRuntime): MoeObject =
    if(array.length == 0) r.NativeObjects.getUndef
    else array.remove(array.length - 1)

  def unshift (r: MoeRuntime, values: MoeObject*): MoeIntObject = {
    array.insertAll(0, values)
    r.NativeObjects.getInt(array.length)
  }

  def push (r: MoeRuntime, values: MoeObject*): MoeIntObject = {
    array ++= values
    r.NativeObjects.getInt(array.length)
  }

  def slice(r: MoeRuntime, indicies: MoeIntObject*): MoeArrayObject = r.NativeObjects.getArray(
    indicies.map(i => at_pos(r, i)) : _*
  )

  def reverse(r: MoeRuntime): MoeArrayObject = r.NativeObjects.getArray(array.reverse:_*)

  def join(r: MoeRuntime, sep: String): String = array.map(_.toString).mkString(sep)

  // MoeObject overrides
  
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '[' + getNativeValue.map(_.toString).mkString(", ") + ']'
  
  // unboxing
  
  override def unboxToList: Try[ArrayBuffer[MoeObject]] = Success(getNativeValue)
}
