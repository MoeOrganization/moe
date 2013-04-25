package org.moe.runtime

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}

/** An object!
 * @param associatedClass The class to associated with this object.
 */
class MoeObject(
    private var associatedType: Option[MoeType] = None
  ) {

  private val id: Int = System.identityHashCode(this)

  /**
   * Returns the ID of this class.
   */
  def getID: Int = id

  /**
   * Returns the class associted with this object, if there is one.
   */
  def getAssociatedClass: Option[MoeClass] = associatedType.flatMap(_.getAssociatedClass)

  /**
   * Returns true if a class is associated with this object.
   */
  def hasAssociatedClass: Boolean = associatedType.exists(_.hasAssociatedClass)

  // associated type ...
  def getAssociatedType: Option[MoeType] = associatedType
  def hasAssociatedType: Boolean = associatedType.isDefined
  def setAssociatedType(t: Option[MoeType]) = associatedType = t

  /**
   * Ask if this object is-a instance of a class
   */
  def isInstanceOf(klassname: String): Boolean = getAssociatedClass.exists(_.isClassOf(klassname))
  def isInstanceOf(klass:   MoeClass): Boolean = getAssociatedClass.exists(_.isClassOf(klass))

  /**
   * Return the name of the class this object is an instance of
   */
  def getClassName: String = getAssociatedClass match {
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
    "{ #instance(" + id + ") " + getAssociatedClass.map({ k => k.toString }).getOrElse("") + "}"
  }

  /**
   * returns true of this is equal to 'that'
   * Should be overridden by subclasses
   */
  def equal_to(that: MoeObject): Boolean = toString == that.toString

  /**
   * returns true of this is not equal to 'that'
   * Should be overridden by subclasses
   */
  def not_equal_to(that: MoeObject): Boolean = toString != that.toString

  // unboxing

  def unboxToBoolean : Try[Boolean] = Success(isTrue)
  def unboxToString  : Try[String]  = Success(toString)        

  def unboxToInt: Try[Int] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Int")
  )    

  def unboxToDouble: Try[Double] = Failure(
    new MoeErrors.IncompatibleType("Cannot convert to Double")
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

  // coercing

  def coerce(r: MoeRuntime, ctx: Option[MoeContext]): MoeObject = {
    ctx match {
      case Some(MoeIntContext())  => r.NativeObjects.getInt(unboxToInt.getOrElse(0))
      case Some(MoeNumContext())  => r.NativeObjects.getNum(unboxToDouble.getOrElse(0.0))
      case Some(MoeBoolContext()) => r.NativeObjects.getBool(unboxToBoolean.get)
      case Some(MoeStrContext())  => r.NativeObjects.getStr(unboxToString.get)
      case None                   => this
      case _                      => throw new MoeErrors.CannotCoerceError(this.toString)
    }
  }

}
