package org.moe.runtime

class MoeParameter(
    private val name: String,
    private val optional: Boolean = false,
    private val slurpy: Boolean = false
  ) extends MoeObject {

  def getName    = name
  def isOptional = optional
  def isSlurpy   = slurpy
}