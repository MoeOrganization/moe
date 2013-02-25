package org.moe.runtime

/**
 * MoeMethod
 *
 * @param name method name
 * @param signature subroutine signature (MoeSignature)
 * @param declaration_env the captured creation environment (MoeEnvironment)
 * @param body executable body of method 
 */

class MoeMethod (
    name: String,
    signature: MoeSignature,
    declaration_env: MoeEnvironment,
    body: (MoeEnvironment) => MoeObject
  ) extends MoeCode(signature, declaration_env, body) {

  private val _name: String = name

  /**
   * Returns the name of this method
   */
  def getName: String = _name

  /**
   * Executes the body of this method passing in a list of arguments
   */
  override def checkParams(args: MoeArguments) = {
    if (!args.hasInvocant)
      throw new MoeErrors.MoeProblems("no invocant set")
    super.checkParams(args)
  } 

  override def prepareEnvironment(e: MoeEnvironment, args: MoeArguments) = {
    e.setCurrentInvocant(args.getInvocant.get)    
    super.prepareEnvironment(e, args)
  }
}
