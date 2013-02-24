package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {
  def getArity  = params.length
  def getParams = params
}