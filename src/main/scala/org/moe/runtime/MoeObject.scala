package org.moe.runtime

/** An object!
 * @param associatedClass The class to associated with this object.
 */
class MoeObject(
    private var associatedClass: Option[MoeClass] = None
  ) {

  private val id: Int = System.identityHashCode(this)

  private var klass: Option[MoeClass] = associatedClass

  /**
   * Returns the ID of this class.
   */
  def getID: Int = id

  /**
   * Returns the class associted with this object, if there is one.
   */
  def getAssociatedClass: Option[MoeClass] = klass

  /**
   * Returns true if a class is associated with this object.
   */
  def hasAssociatedClass: Boolean = klass.isDefined

  /**
   * Set the class that is associated with this object.
   *
   * @param c The class to associate with this object.
   */
  def setAssociatedClass(c: Option[MoeClass]) = klass = c

  /**
   * Invoke the named method.
   *
   * @param name The name of the method
   */
  def callMethod(name: String): MoeObject = callMethod(name = name, args = List())

  /**
   * Invoke the named method with the supplied arguments.
   *
   * @param name The name of the method
   * @param args The list of arguments to provide to the method
   */
  def callMethod(name: String, args: List[MoeObject]): MoeObject = {
    klass.map({ k =>
      k.getMethod(name).map({ m =>
        m.execute(this, args)
      }).getOrElse(
        throw new MoeRuntime.Errors.MethodNotFound(name)
      )
    }).getOrElse(
      throw new MoeRuntime.Errors.MissingClass(toString)
    )
  }

  /**
   * Returns true.
   */
  def isTrue: Boolean = !isFalse

  /**
   * Returns false.
   */
  def isFalse: Boolean = false

  /**
   * Returns false
   */
  def isUndef: Boolean = false

  /**
   * Returns a string representation of this object.
   */
  override def toString: String = {
    "{ #instance(" + id + ")" + klass.map({ k => k.toString }).getOrElse("") + " }"
  }
}