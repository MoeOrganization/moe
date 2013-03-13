package org.moe.runtime

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

trait UnboxMoeToNative {
  def unboxToBoolean: Try[Boolean] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Boolean")
  )    

  def unboxToString: Try[String] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to String")
  )    

  def unboxToInt: Try[Int] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Int")
  )    

  def unboxToDouble: Try[Double] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Double")
  )

  def unboxToNull: Try[Null] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Null")
  )

  def unboxToArrayBuffer: Try[ArrayBuffer[MoeObject]] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to ArrayBuffer[MoeObject]")
  )

  def unboxToMap: Try[HashMap[String, MoeObject]] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to HashMap[String, MoeObject]")
  )

  def unboxToTuple: Try[(String, MoeObject)] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to (String, MoeObject)")
  )
}

/** An object!
 * @param associatedClass The class to associated with this object.
 */
class MoeObject(
    private var associatedClass: Option[MoeClass] = None
  ) extends UnboxMoeToNative {

  private val id: Int = System.identityHashCode(this)

  /**
   * Returns the ID of this class.
   */
  def getID: Int = id

  /**
   * Returns the class associted with this object, if there is one.
   */
  def getAssociatedClass: Option[MoeClass] = associatedClass

  /**
   * Returns true if a class is associated with this object.
   */
  def hasAssociatedClass: Boolean = associatedClass.isDefined

  /**
   * Set the class that is associated with this object.
   *
   * @param c The class to associate with this object.
   */
  def setAssociatedClass(c: Option[MoeClass]) = associatedClass = c

  /**
   * Ask if this object is-a instance of a class
   */
  def isInstanceOf(klassname: String): Boolean = associatedClass.exists(_.isClassOf(klassname))
  def isInstanceOf(klass:   MoeClass): Boolean = associatedClass.exists(_.isClassOf(klass))

  /**
   * Return the name of the class this object is an instance of
   */
  def getClassName: String = associatedClass match {
    case Some(klass) => klass.getName
    case _ => ""
  }

  /**
   * Invoke the named method.
   *
   * @param name The name of the method
   */
  def callMethod(method: MoeMethod): MoeObject = callMethod(method, List())

  /**
   * Invoke the named method with the supplied arguments.
   *
   * @param name The name of the method
   * @param args The list of arguments to provide to the method
   */
  def callMethod(method: MoeMethod, args: List[MoeObject]): MoeObject = method.execute(new MoeArguments(args, Some(this)))

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
    "{ #instance(" + id + ")" + associatedClass.map({ k => k.toString }).getOrElse("") + " }"
  }

  // unboxing

  override def unboxToBoolean: Try[Boolean] = Success(isTrue)
  override def unboxToString: Try[String] = Success(toString)

}
