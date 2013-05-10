package org.moe.runtime

object MoeSubroutineTraits extends Enumeration {
  type MoeSubroutineTraits = Value
  val Export = Value("export") // should be exported if the containing package is used
}

import MoeSubroutineTraits._

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
    traits: List[MoeSubroutineTraits] = List()
  ) extends MoeCode(signature, declaration_env, body, Some(name)) { 

  private val _traits: List[MoeSubroutineTraits] = traits

  /**
   * Returns the traits associated with this subroutine
   */
  def getTraits: List[MoeSubroutineTraits] = _traits

  def hasTrait(t: MoeSubroutineTraits): Boolean = _traits.contains(t)
  
}
