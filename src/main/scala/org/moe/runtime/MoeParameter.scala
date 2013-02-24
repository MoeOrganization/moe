package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeParameter(
    private val name: String
    //private val optional: Boolean = false,
    //private val default: () => Option[MoeObject] = () => None
  ) {
  def getName = name
  //def isOptional = optional
  //def getDefault: Option[MoeObject] = default()
}