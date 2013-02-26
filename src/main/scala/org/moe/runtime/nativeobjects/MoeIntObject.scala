package org.moe.runtime.nativeobjects

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import scala.util.{Try, Success, Failure}

class MoeIntObject(
    v: Int, klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Int](v, klass) {

  // runtime methods

  def increment (r: MoeRuntime): Unit = setNativeValue(getNativeValue + 1)
  def decrement (r: MoeRuntime): Unit = setNativeValue(getNativeValue - 1)

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

  // MoeObject overrides

  override def isFalse: Boolean = getNativeValue == 0
  override def toString = getNativeValue.toString

  // unboxing
  
  override def unboxToInt: Try[Int] = Success(getNativeValue)
  override def unboxToDouble: Try[Double] = Success(getNativeValue.toDouble)

}