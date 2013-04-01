package org.moe.runtime.nativeobjects

import org.moe.runtime._

import scala.util.{Try, Success, Failure}

class MoeNumObject(
    v: Double, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Double](v, klass) {

  // runtime methods

  def increment (r: MoeRuntime): Unit = setNativeValue(getNativeValue + 1.0)
  def decrement (r: MoeRuntime): Unit = setNativeValue(getNativeValue - 1.0)

  // math

  def add (r: MoeRuntime, other: MoeObject): MoeObject = r.NativeObjects.getNum(
    getNativeValue + other.unboxToDouble.get
  )

  def subtract (r: MoeRuntime, other: MoeObject): MoeObject = r.NativeObjects.getNum(
    getNativeValue - other.unboxToDouble.get
  )

  def multiply (r: MoeRuntime, other: MoeObject): MoeObject = r.NativeObjects.getNum(
    getNativeValue * other.unboxToDouble.get
  )

  def divide (r: MoeRuntime, other: MoeObject): MoeNumObject = r.NativeObjects.getNum(
    getNativeValue / other.unboxToDouble.get
  )

  def modulo (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    MoeUtil.perlModuloOp(unboxToInt.get, other.unboxToInt.get)
  )

  def pow (r: MoeRuntime, other: MoeObject): MoeObject = r.NativeObjects.getNum(
    Math.pow(getNativeValue, other.unboxToDouble.get)
  )

  // equality

  def equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue == other.unboxToDouble.get
  )

  def not_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue != other.unboxToDouble.get
  )

  // comparison

  def less_than (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue < other.unboxToDouble.get
  )

  def greater_than (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue > other.unboxToDouble.get
  )

  def less_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue <= other.unboxToDouble.get
  )

  def greater_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = r.NativeObjects.getBool(
    getNativeValue >= other.unboxToDouble.get
  )

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue == 0
  override def toString = getNativeValue.toString

  // unboxing

  override def unboxToInt: Try[Int] = Success(getNativeValue.toInt)
  override def unboxToDouble: Try[Double] = Success(getNativeValue)

  // coercion

  def toInt(r: MoeRuntime): MoeIntObject =
    r.NativeObjects.getInt(
      unboxToInt match {
        case Success(i) => i
        case Failure(e) => 0
      }
    )

  def toBool(r: MoeRuntime): MoeBoolObject =
    r.NativeObjects.getBool(if (getNativeValue == 0) false else true)

  def toStr(r: MoeRuntime): MoeStrObject =
    r.NativeObjects.getStr(toString)

  def coerce(r: MoeRuntime, ctx: Option[MoeContext]): MoeObject = {
    ctx match {
      case Some(MoeIntContext())  => toInt(r)
      case Some(MoeNumContext())  => this
      case Some(MoeBoolContext()) => toBool(r)
      case Some(MoeStrContext())  => toStr(r)
      case None                   => this
      case _                      => throw new MoeErrors.CannotCoerceError(this.toString)
    }
  }
}
