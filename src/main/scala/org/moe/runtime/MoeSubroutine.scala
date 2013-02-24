package org.moe.runtime

/**
 * MoeSubroutine: Class for moe subroutine!
 *
 * @param name method name
 * @param body executable body of method 
 */

class MoeSubroutine (
    private val name: String,
    private val signature: MoeSignature,
    private val body: (MoeEnvironment) => MoeObject
  ) extends MoeObject {

  /**
   * Returns the name of this subroutine
   */
  def getName: String = name

  /**
   * Returns the signature of this subroutine
   */
  def getSignature: MoeSignature = signature

  /**
   * Returns the executable body of this subroutine
   */
  def getBody: (MoeEnvironment) => MoeObject = body

  /**
   * Executes the body of this subroutine passing in a list of arguments
   */
  def execute (env: MoeEnvironment, args : List[MoeObject]): MoeObject = {

    if (signature.getArity != args.length) 
      throw new MoeErrors.MoeProblems("not enought arguments")

    val params = signature.getParams

    for (i <- 0.until(signature.getArity)) {
      env.create(params(i), args(i))
    }

    body(env)
  }
}
