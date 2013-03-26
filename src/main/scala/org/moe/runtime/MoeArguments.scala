package org.moe.runtime

class MoeArguments(
    private val args: List[MoeObject] = List(),
    private val invocant: Option[MoeObject] = None
  ) {

  def getInvocant: Option[MoeObject] = invocant
  def hasInvocant: Boolean = invocant.isDefined

  def getArgCount: Int = args.length

  def getArgAt(i: Int): Option[MoeObject] = if (i < args.length) Some(args(i)) else None
  
  def slurpArgsAt(i: Int): List[MoeObject] = args.drop(i)
}