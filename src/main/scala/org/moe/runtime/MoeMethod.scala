package org.moe.runtime

/**
 * MoeMethod
 *
 * @param name method name
 * @param body executable body of method 
 */

class MoeMethod (
    private val name: String,
    private val body: (MoeObject, List[MoeObject]) => MoeObject
  ) extends MoeObject {

  /**
   * Returns the name of this method
   */
  def getName: String = name

  /**
   * Returns the executable body of this method
   */
  def getBody: (MoeObject, List[MoeObject]) => MoeObject = body

  /**
   * Executes the body of this method passing in a list of arguments
   */
  def execute(invocant: MoeObject, args: List[MoeObject]): MoeObject = body(invocant, args)
}
