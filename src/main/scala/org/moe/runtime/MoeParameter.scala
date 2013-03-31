package org.moe.runtime

sealed abstract class MoeParameter(private val name: String) extends MoeObject {
  def getName    = name
  def getKeyName = name.drop(1)
}

case class MoePositionalParameter  (val n: String) extends MoeParameter(n)
case class MoeOptionalParameter    (val n: String) extends MoeParameter(n)
case class MoeSlurpyParameter      (val n: String) extends MoeParameter(n)
case class MoeNamedParameter       (val n: String) extends MoeParameter(n)
case class MoeSlurpyNamedParameter (val n: String) extends MoeParameter(n)
