package org.moe.runtime

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {
  def getArity  = params.length
  def getParams = params
}