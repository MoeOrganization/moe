package org.moe.runtime

import scala.util.{Try, Success, Failure}

abstract class MoeNativeObject[A] (
    private val value: A,
    private var associatedClass: Option[MoeClass] = None
  ) extends MoeObject(associatedClass) {

  def getNativeValue: A = value
}

