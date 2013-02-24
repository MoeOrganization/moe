package org.moe.runtime

/**
 * MoeSubroutine: Class for moe subroutine!
 *
 * @param name method name
 * @param body executable body of method 
 */

class MoeSubroutine (
    private val name: String,
    private val body: (List[MoeObject]) => MoeObject
  ) extends MoeObject {

  /**
   * Returns the name of this subroutine
   */
  def getName: String = name

  /**
   * Returns the executable body of this subroutine
   */
  def getBody: (List[MoeObject]) => MoeObject = body

  /**
   * Executes the body of this subroutine passing in a list of arguments
   */
  def execute (args : List[MoeObject]): MoeObject = body(args)
}
