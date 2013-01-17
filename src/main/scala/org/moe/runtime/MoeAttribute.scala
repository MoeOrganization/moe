package org.moe.runtime

/**
 * An Attribute!
 *
 * @param name The name of this attribute
 * @param default Default value for this attribute
 */
class MoeAttribute(
    private val name:    String,
    private val default: MoeObject
  ) extends MoeObject {

  /**
   * Return the name of this attribute.
   */
  def getName: String = name

  /**
   * Return default value of this attribute.
   */
  def getDefault: MoeObject = default
}

/*

NOTES:

- the default value really should be cloned
  but the question is how to actually go about
  this, so I am punting for the time being

*/