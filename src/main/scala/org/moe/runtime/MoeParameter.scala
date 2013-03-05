package org.moe.runtime

class MoeParameter(
    private val name: String
    //private val optional: Boolean = false,
    //private val default: () => Option[MoeObject] = () => None
  ) extends MoeObject {
  def getName = name
  //def isOptional = optional
  //def getDefault: Option[MoeObject] = default()
}