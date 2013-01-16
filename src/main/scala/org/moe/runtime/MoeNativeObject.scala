package org.moe.runtime

abstract class MoeNativeObject[A] (
  private val value: A,
  private var associatedClass: Option[MoeClass] = None)
  extends MoeObject(associatedClass) {

  def getNativeValue: A = value
}

// NOTE:
// It will likely be useful to add more methods
// to all of these classes, if for no other reason
// then to keep the boxing/unboxing logic in one
// single place. At which point, it will also
// become useful to break these out into separate
// source files as well.
// - SL

// Simple objects

class MoeIntObject(v: Int) extends MoeNativeObject[Int](v) {
  override def isFalse: Boolean = getNativeValue == 0
}

class MoeFloatObject(v: Double) extends MoeNativeObject[Double](v) {
  override def isFalse: Boolean = getNativeValue == 0
}

class MoeStringObject(v: String) extends MoeNativeObject[String](v) {
  override def isFalse: Boolean = getNativeValue match {
    case "" | "0" => true
    case _        => false
  }
}

class MoeBooleanObject(v: Boolean) extends MoeNativeObject[Boolean](v) {
  override def isFalse: Boolean = getNativeValue == false
}

// this is the one outlyer, it doesn't need to be
// a NativeObject because it doesn't need to actually
// box any values, just the lack of a value
class MoeNullObject extends MoeObject {
  def getNativeValue: AnyRef  = null
  override def isFalse: Boolean = true
  override def isUndef: Boolean = true
}

// Complex objects

// NOTE:
// List[A] is a poor internal representation
// for a MoeArray, this will need to be thought
// through much more. It looks like ListBuffer
// could be suitable, but we might need to just
// write our own in the long run.
// - SL
class MoeArrayObject(v: List[MoeObject]) extends MoeNativeObject[List[MoeObject]](v) {
  override def isFalse: Boolean = getNativeValue.size == 0
}

class MoeHashObject(v: Map[String, MoeObject]) extends MoeNativeObject[Map[String, MoeObject]](v) {
  override def isFalse: Boolean = getNativeValue.size == 0
}

class MoePairObject(v: (String, MoeObject)) extends MoeNativeObject[(String, MoeObject)](v) {}
