package org.moe.runtime

/**
 * MoeAttribute
 *
 * @param name MoeAttribute
 * @param default None
 */
class MoeAttribute(
    private val name: String,
    private val default: Option[MoeObject] = None
  ) extends MoeObject {

  /**
   * Return the name of this attribute.
   */
  def getName: String = name

  /**
   * Returns true if this attribute has a default set.
   */
  def hasDefault: Boolean = default.isDefined

  /**
   * Return default value of this attribute.
   */
  def getDefault: Option[MoeObject] = default
}

/*

NOTES:

- the default value really should be cloned
  but the question is how to actually go about
  this, so I am punting for the time being

*/
