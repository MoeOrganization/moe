package org.moe.runtime

class MoeObject {

  private val id: Integer = System.identityHashCode(this)

  private var klass: MoeClass = _

  def this (k: MoeClass) = {
    this()
    klass = k
  }

  def getID: Integer  = id
  def getAssociatedClass: MoeClass = klass
  def hasAssociatedClass: Boolean  = klass != null

  def setAssociatedClass(k: MoeClass): Unit = klass = k

  def callMethod(method: String): MoeObject = callMethod(method, List())
  def callMethod(method: String, args: List[MoeObject]): MoeObject = {
    if (klass == null) throw new Runtime.Errors.MissingClass(toString)
    klass.getMethod(method).execute(this, args)
  }

  def isTrue: Boolean = !isFalse
  def isFalse: Boolean = false
  def isUndef: Boolean = false

  override def toString: String = {
    var out = "{ #instance(" + id + ")"
    if (hasAssociatedClass) out += " " + klass.toString
    out + " }"
  }
}