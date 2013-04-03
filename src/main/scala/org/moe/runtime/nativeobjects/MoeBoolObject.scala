package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.util.{Try, Success, Failure}

class MoeBoolObject(
    v: Boolean, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Boolean](v, klass) {

  // runtime methods

  def toInt: Int = if (getNativeValue) 1 else 0

  override def isFalse: Boolean = getNativeValue == false
  override def toString: String = if (getNativeValue) "true" else "false"

  // unboxing

  override def unboxToInt    : Try[Int]    = Try(toInt)
  override def unboxToDouble : Try[Double] = Try(toInt.toDouble)

  // coercion

  def toInt(r: MoeRuntime): MoeIntObject =
    r.NativeObjects.getInt(
      unboxToInt match {
        case Success(i) => i
        case Failure(e) => 0
      }
    )

  def toNum(r: MoeRuntime): MoeNumObject =
    r.NativeObjects.getNum(
      unboxToDouble match {
        case Success(d) => d
        case Failure(e) => 0.0
      }
    )

  def toStr(r: MoeRuntime): MoeStrObject =
    r.NativeObjects.getStr(toString)

  def coerce(r: MoeRuntime, ctx: Option[MoeContext]): MoeObject = {
    ctx match {
      case Some(MoeIntContext())  => toInt(r)
      case Some(MoeNumContext())  => toNum(r)
      case Some(MoeBoolContext()) => this
      case Some(MoeStrContext())  => toStr(r)
      case None                   => this
      case _                      => throw new MoeErrors.CannotCoerceError(this.toString)
    }
  }
}
