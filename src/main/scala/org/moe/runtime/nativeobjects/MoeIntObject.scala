package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.util.{Try, Success, Failure}

class MoeIntObject(
    v: Int, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Int](v, klass) {

  // runtime methods

  def negate (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(getNativeValue * -1)

  def increment (r: MoeRuntime): MoeIntObject = { setNativeValue(getNativeValue + 1); this }
  def decrement (r: MoeRuntime): MoeIntObject = { setNativeValue(getNativeValue - 1); this }

  // math

  def add (r: MoeRuntime, other: MoeObject): MoeObject = other match {
    case i: MoeIntObject => r.NativeObjects.getInt(getNativeValue    + i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getNum(unboxToDouble.get + f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def subtract (r: MoeRuntime, other: MoeObject): MoeObject = other match {
    case i: MoeIntObject => r.NativeObjects.getInt(getNativeValue    - i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getNum(unboxToDouble.get - f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def multiply (r: MoeRuntime, other: MoeObject): MoeObject = other match {
    case i: MoeIntObject => r.NativeObjects.getInt(getNativeValue    * i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getNum(unboxToDouble.get * f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def divide (r: MoeRuntime, other: MoeObject): MoeNumObject = r.NativeObjects.getNum(
    getNativeValue / other.unboxToDouble.get
  )

  def modulo (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    MoeUtil.perlModuloOp(getNativeValue, other.unboxToInt.get)
  )

  def pow (r: MoeRuntime, other: MoeObject): MoeObject = other match {
    case i: MoeIntObject => r.NativeObjects.getInt(Math.pow(getNativeValue, i.unboxToInt.get).toInt)
    case f: MoeNumObject => r.NativeObjects.getNum(Math.pow(getNativeValue, f.unboxToDouble.get))
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  // equality

  def equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    == i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get == f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def not_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    != i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get != f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def compare_to (r: MoeRuntime, other: MoeObject): MoeIntObject = other match {
    case i: MoeIntObject => r.NativeObjects.getInt(getNativeValue    compareTo i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getInt(unboxToDouble.get compareTo f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  // comparison

  def less_than (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    < i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get < f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def greater_than (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    > i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get > f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def less_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    <= i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get <= f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  def greater_than_or_equal_to (r: MoeRuntime, other: MoeObject): MoeBoolObject = other match {
    case i: MoeIntObject => r.NativeObjects.getBool(getNativeValue    >= i.unboxToInt.get)
    case f: MoeNumObject => r.NativeObjects.getBool(unboxToDouble.get >= f.unboxToDouble.get)
    case _               => throw new MoeErrors.UnexpectedType(other.toString)
  }

  // bitwise operators
  def bit_and (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    getNativeValue & other.unboxToInt.get
  )

  def bit_or (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    getNativeValue | other.unboxToInt.get
  )

  def bit_xor (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    getNativeValue ^ other.unboxToInt.get
  )

  def bit_shift_left (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    getNativeValue << other.unboxToInt.get
  )

  def bit_shift_right (r: MoeRuntime, other: MoeObject): MoeIntObject = r.NativeObjects.getInt(
    getNativeValue >> other.unboxToInt.get
  )

  // methods

  def abs (r: MoeRuntime): MoeIntObject = r.NativeObjects.getInt(Math.abs(getNativeValue))

  def sin (r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.sin(unboxToDouble.get))
  def cos (r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.cos(unboxToDouble.get))
  def tan (r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.tan(unboxToDouble.get))
  def asin(r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.asin(unboxToDouble.get))
  def acos(r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.acos(unboxToDouble.get))
  def atan(r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.atan(unboxToDouble.get))
  def atan2(r: MoeRuntime, other: MoeObject): MoeNumObject =
    r.NativeObjects.getNum(Math.atan2(unboxToDouble.get, other.unboxToDouble.get))

  def exp (r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.exp(unboxToDouble.get))
  def log (r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.log(unboxToDouble.get))
  def sqrt(r: MoeRuntime): MoeNumObject = r.NativeObjects.getNum(Math.sqrt(unboxToDouble.get))

  // MoeNativeObject overrides

  override def copy = new MoeIntObject(getNativeValue, getAssociatedClass)

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue == 0
  override def toString = getNativeValue.toString
  override def isScalar = true

  // unboxing

  override def unboxToInt: Try[Int] = Success(getNativeValue)
  override def unboxToDouble: Try[Double] = Success(getNativeValue.toDouble)

  // coercion

  def toNum(r: MoeRuntime): MoeNumObject =
    r.NativeObjects.getNum(
      unboxToDouble match {
        case Success(d) => d
        case Failure(e) => 0.0
      }
    )

  def toBool(r: MoeRuntime): MoeBoolObject =
    r.NativeObjects.getBool(if (getNativeValue == 0) false else true)

  def toStr(r: MoeRuntime): MoeStrObject =
    r.NativeObjects.getStr(toString)

  def coerce(r: MoeRuntime, ctx: Option[MoeContext]): MoeObject = {
    ctx match {
      case Some(MoeIntContext())  => this
      case Some(MoeNumContext())  => toNum(r)
      case Some(MoeBoolContext()) => toBool(r)
      case Some(MoeStrContext())  => toStr(r)
      case None                   => this
      case _                      => throw new MoeErrors.CannotCoerceError(this.toString)
    }
  }
}
