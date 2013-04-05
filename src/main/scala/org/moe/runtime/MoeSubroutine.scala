package org.moe.runtime

/**
 * MoeSubroutine: Class for moe subroutine!
 *
 * @param name subroutine name
 * @param signature subroutine signature (MoeSignature)
 * @param declaration_env the captured creation environment (MoeEnvironment)
 * @param body executable body of method 
 */

class MoeSubroutine (
    name: String,
    signature: MoeSignature,
    declaration_env: MoeEnvironment,
    body: (MoeEnvironment) => MoeObject,
    traits: List[String] = List()
  ) extends MoeCode(signature, declaration_env, body) { 

  private val _traits: List[String] = traits
  private val _name: String = name

  /**
   * Returns the traits associated with this subroutine
   */
  def getTraits: List[String] = _traits


  /**
   * Returns the name of this subroutine
   */
  def getName: String = _name
}
