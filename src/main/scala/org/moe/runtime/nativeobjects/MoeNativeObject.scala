package org.moe.runtime

abstract class MoeNativeObject[A] (
    private var value: A,
    private var associatedType: Option[MoeType] = None
  ) extends MoeObject(associatedType) {

  def getNativeValue: A = value
  protected def setNativeValue(v: A) = value = v

  def copy: MoeNativeObject[A]
}

