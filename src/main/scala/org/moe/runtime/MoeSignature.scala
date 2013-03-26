package org.moe.runtime

class MoeSignature(
    private val params: List[MoeParameter] = List()
  ) extends MoeObject {

  def getParams = params

  def getArity = params.length

}