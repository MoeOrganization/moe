package org.moe.runtime

class MoeObject {

  private val id: Int = System.identityHashCode(this)

  private var klass: Option[MoeClass] = None

  def this (k: Option[MoeClass]) = {
    this()
    klass = k
  }

  def getID: Int = id
  def getAssociatedClass: Option[MoeClass] = klass
  def hasAssociatedClass: Boolean = klass.isDefined

  def setAssociatedClass(k: Option[MoeClass]) = klass = k

  def callMethod(method: String): MoeObject = callMethod(method, List())
  def callMethod(method: String, args: List[MoeObject]): MoeObject = {
    klass.map({ k => k.getMethod(method).execute(this, args) }).getOrElse(
      throw new Runtime.Errors.MissingClass(toString)
    )
  }

  def isTrue: Boolean = !isFalse
  def isFalse: Boolean = false
  def isUndef: Boolean = false

  override def toString: String = {
    "{ #instance(" + id + ")" + klass.map({ k => k.toString }).getOrElse("") + "}"
  }
}