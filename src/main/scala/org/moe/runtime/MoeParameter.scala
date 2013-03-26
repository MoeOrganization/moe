package org.moe.runtime

sealed abstract class MoeParameter(private val name: String) extends MoeObject {
  def getName = name
}

case class MoeNamedParameter(val n: String) extends MoeParameter(n)

case class MoeOptionalParameter(val n: String) extends MoeParameter(n)

case class MoeSlurpyParameter(val n: String) extends MoeParameter(n)