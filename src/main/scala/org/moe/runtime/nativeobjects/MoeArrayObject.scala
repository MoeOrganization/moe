package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

// NOTE:
// List[A] is a poor internal representation
// for a MoeArray, this will need to be thought
// through much more. It looks like ListBuffer
// could be suitable, but we might need to just
// write our own in the long run.
// - SL

class MoeArrayObject(
    v: List[MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[List[MoeObject]](v, klass) {

  private def array = getNativeValue

  // Runtime methods
  
  def at_pos (r: MoeRuntime, i: MoeIntObject): MoeObject = array(i.unboxToInt.get)
	
  // MoeObject overrides
  
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '[' + getNativeValue.map(_.toString).mkString(", ") + ']'
  
  // unboxing
  
  override def unboxToList: Try[List[MoeObject]] = Success(getNativeValue)
}
