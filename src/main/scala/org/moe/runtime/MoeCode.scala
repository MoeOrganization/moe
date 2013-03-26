package org.moe.runtime

/**
 * MoeCode
 *
 * @param signature subroutine signature (MoeSignature)
 * @param declaration_env the captured creation environment (MoeEnvironment)
 * @param body executable body of method 
 */

class MoeCode (
    private val signature: MoeSignature,
    private val declaration_env: MoeEnvironment,
    private val body: (MoeEnvironment) => MoeObject
  ) extends MoeObject {

  /**
   * Returns the signature of this method
   */
  def getSignature: MoeSignature = signature

  /**
   * Returns the environment this was declared in 
   */
  def getDeclarationEnvironment: MoeEnvironment = declaration_env

  /**
   * Returns the executable body of this method
   */
  def getBody: (MoeEnvironment) => MoeObject = body

  /**
   * Execute the body (assuming the environment is all prepared for you)
   */
  def executeBody(e: MoeEnvironment): MoeObject = body(e)
  
  /**
   * Executes the body of this code
   */
  def execute (args : MoeArguments): MoeObject = executeBody(prepareExecutionEnvironment(args))

  protected def prepareExecutionEnvironment(args: MoeArguments): MoeEnvironment = {
    val env = new MoeEnvironment(Some(declaration_env))
    signature.checkArguments(args)
    signature.bindArgsToEnv(args, env)
    env
  }
}
