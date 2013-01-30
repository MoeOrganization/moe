package org.moe.runtime

/**
 * MoeSubroutine: Class for moe subroutine!
 *
 * @param name MoeSubroutine
 * @param version None
 * @param authority None
 * @param superclass None
 */

class MoeSubroutine (
    private val name: String,
    private val body: (List[MoeObject]) => MoeObject
  ) extends MoeObject {

  def this (name: String) = {
    // yadda yadda yadda
    this(name, (args) => throw new MoeRuntime.Errors.UndefinedSubroutine(name))
  }

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
