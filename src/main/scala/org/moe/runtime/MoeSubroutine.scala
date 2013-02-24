package org.moe.runtime

/**
 * MoeSubroutine: Class for moe subroutine!
 *
 * @param name method name
 * @param body executable body of method 
 */

class MoeSubroutine (
    private val name: String,
    private val signature: MoeSignature, // = new MoeSignature(List(new MoeParameter("@_"))),
    private val captured_env: MoeEnvironment,
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
   * Returns the captured environment in which this subroutine was created
   */
  def getCapturedEnvironment: MoeEnvironment = captured_env

  /**
   * Returns the executable body of this subroutine
   */
  def getBody: (MoeEnvironment) => MoeObject = body

  /**
   * Executes the body of this subroutine passing in a list of arguments
   */
  def execute (args : List[MoeObject]): MoeObject = {

    val exec_env = new MoeEnvironment(Some(captured_env))

    if (signature.getArity != args.length) 
      throw new MoeErrors.MoeProblems("not enought arguments")

    val params = signature.getParams

    for (i <- 0.until(signature.getArity)) {
      exec_env.create(params(i).getName, args(i))
    }

    body(exec_env)
  }
}
