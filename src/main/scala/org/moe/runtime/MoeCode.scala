package org.moe.runtime

/**
 * MoeCode
 *
 * @param signature subroutine signature (MoeSignature)
 * @param declaration_env the captured creation environment (MoeEnvironment)
 * @param body executable body of method 
 */

class MoeCode (
    private val signature: MoeSignature, // = new MoeSignature(List(new MoeParameter("@_"))),
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
  def execute (args : MoeArguments): MoeObject = {
    val exec_env = new MoeEnvironment(Some(declaration_env))
    checkParams(args)
    prepareEnvironment(exec_env, args)
    executeBody(exec_env)
  }

  protected def checkParams(args: MoeArguments) = {
    if (signature.getArity != args.getArgCount) 
      throw new MoeErrors.MoeProblems("not enought arguments")
  } 

  protected def prepareEnvironment(e: MoeEnvironment, args: MoeArguments) = {
    val params = signature.getParams

    for (i <- 0.until(signature.getArity)) {
      e.create(params(i).getName, args.getArgAt(i))
    }
  }
}
