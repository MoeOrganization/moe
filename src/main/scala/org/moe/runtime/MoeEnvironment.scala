package org.moe.runtime

import scala.collection.mutable.HashMap

import org.moe.runtime._

/**
 * MoeEnvironment: Presumably the moe environment
 *
 * @param parent optional parent environment
 */

class MoeEnvironment(private val parent: Option[MoeEnvironment] = None) {

  object Markers {
    val Package  = "__PACKAGE__"
    val Class    = "__CLASS__"
    val Invocant = "__SELF__"
  }

  private val pad = new HashMap[String, MoeObject]()

  def getCurrentPackage: MoePackage = getLocal(Markers.Package).asInstanceOf[MoePackage]
  def getCurrentClass: MoeClass = getLocal(Markers.Class).asInstanceOf[MoeClass]
  def getCurrentInvocant: MoeObject  = getLocal(Markers.Invocant)

  def setCurrentPackage(p: MoeObject): Unit = setLocal(Markers.Package, p)
  def setCurrentClass(c: MoeObject): Unit = setLocal(Markers.Class, c)
  def setCurrentInvocant(i: MoeObject): Unit = setLocal(Markers.Invocant, i)


  def getParent: Option[MoeEnvironment] = parent
  def isRoot: Boolean = !parent.isDefined

  def get(name: String): MoeObject = {
    if (hasLocal(name)) return getLocal(name)
    parent match {
      case Some(p) => p.get(name)
      case None => throw new Runtime.Errors.ValueNotFound(name)
    }
  }

  def has(name: String): Boolean = {
    if (hasLocal(name)) return true
    parent match {
      case Some(p) => p.has(name)
      case None => false
    }
  }

  def create(name: String, value: MoeObject): Unit = setLocal(name, value)

  def set(name: String, value: MoeObject): Unit = {
    // This env and non of it's parents know about this value, explode
    if (!has(name)) throw new Runtime.Errors.UndefinedValue(name)
    if (hasLocal(name)) {
      // This environment has a local value, set it
      setLocal(name, value)
    } else {
      // If we have a parent, reach into it to set (recursing upward). If
      // we don't then blow up with an UndefinedValue
      parent match {
        case Some(p) => p.set(name, value)
        case None => throw new Runtime.Errors.UndefinedValue(name)
      }
      // The above could be more idiomatic as:
      // parent.map({ p => p.set(name, value) }).getOrElse(
      //   throw new Runtime.Errors.UndefinedValue(name)
      // )
    }
  }

  private def getLocal(name: String): MoeObject = pad(name)
  private def hasLocal(name: String): Boolean   = pad.contains(name)
  private def setLocal(name: String, value: MoeObject): Unit = pad += (name -> value)

}
