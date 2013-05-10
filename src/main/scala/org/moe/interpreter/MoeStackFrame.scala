package org.moe.interpreter

import org.moe.runtime._

class MoeStackFrame (
    private val routine   : MoeCode,
    private val arguments : List[MoeObject],
    private val env       : MoeEnvironment,
    private val invocant  : Option[MoeObject] = None
  ) {

  def getCurrentPackage: Option[MoePackage] = routine.getDeclarationEnvironment.getCurrentPackage

  def getCurrentClass: Option[MoeClass] = routine.getDeclarationEnvironment.getCurrentClass

  def getCurrentInvocant = invocant

  def getCallSiteEnvironment = env

  def getCode = routine
  def getArgs = arguments
}