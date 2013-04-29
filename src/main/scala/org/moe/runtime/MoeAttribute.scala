package org.moe.runtime

/**
 * MoeAttribute
 *
 * @param name MoeAttribute
 * @param default None
 */
class MoeAttribute(
    private val name: String,
    private val default: Option[() => MoeObject] = None
  ) extends MoeObject {

  /**
   * Return the name of this attribute.
   */
  def getName: String = name
  def getKeyName = name.drop(2)

  /**
   * Returns true if this attribute has a default set.
   */
  def hasDefault: Boolean = default.isDefined

  /**
   * Return default value of this attribute.
   */
  def getDefault: Option[MoeObject] = default.map(x => x())
}

/**

package Moe::Runtime {
    
    class MoeAttribute {
      
        has $!name;
        has &!default;

        method get_name { $!name }
        method get_key_name {
          $!name.substr( 1, $!name.length )
        }

        method has_default { &!default.defined }
        method get_default { &!default.call()  }
    }
}

**/