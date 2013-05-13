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
    private val body: (MoeEnvironment) => MoeObject,
    private val name: Option[String] = None
  ) extends MoeObject {

  /**
   * Returns the name of this method
   */
  def getName: String = name.getOrElse("__ANON__")

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
  def executeBody(e: MoeEnvironment): MoeObject = {
    e.setCurrentRoutine(this)
    val r = body(e)
    e.clearCurrentRoutine
    r
  }
  
  /**
   * Executes the body of this code
   */
  def execute (args : MoeArguments): MoeObject = executeBody(prepareExecutionEnvironment(args))

  protected def prepareExecutionEnvironment(args: MoeArguments): MoeEnvironment = {
    val env = new MoeEnvironment(Some(declaration_env))
    signature.bindArgsToEnv(args, env)
    env
  }
}
