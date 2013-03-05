package org.moe.runtime

class MoeArguments(
    private val args: List[MoeObject],
    private val invocant: Option[MoeObject] = None
  ) {

  def getInvocant: Option[MoeObject] = invocant
  def hasInvocant: Boolean = invocant.isDefined

  def getArgs: List[MoeObject] = args
  def getArgAt(i: Int): MoeObject = args(i)
  def getArgCount: Int = args.length
}