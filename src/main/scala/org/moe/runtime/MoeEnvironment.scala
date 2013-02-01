package org.moe.runtime

import scala.collection.mutable.{Map,HashMap}

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
    val Topic    = "$_"
  }

  private val pad: Map[String, MoeObject] = new HashMap[String, MoeObject]()

  def getCurrentPackage: MoePackage = get(Markers.Package).asInstanceOf[MoePackage]
  def getCurrentClass: MoeClass     = get(Markers.Class).asInstanceOf[MoeClass]
  def getCurrentInvocant: MoeObject = get(Markers.Invocant)
  def getCurrentTopic: MoeObject    = get(Markers.Topic)

  def setCurrentPackage(p: MoeObject): Unit  = setLocal(Markers.Package, p)
  def setCurrentClass(c: MoeObject): Unit    = setLocal(Markers.Class, c)
  def setCurrentInvocant(i: MoeObject): Unit = setLocal(Markers.Invocant, i)
  def setCurrentTopic(t: MoeObject): Unit    = setLocal(Markers.Topic, t)

  def getParent  = parent
  def isRoot     = !parent.isDefined

  def get(name: String): MoeObject = {
    if (hasLocal(name)) return getLocal(name)
    parent match {
      case Some(p) => p.get(name)
      case None => throw new MoeRuntime.Errors.ValueNotFound(name)
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
    if (!has(name)) throw new MoeRuntime.Errors.UndefinedValue(name)
    if (hasLocal(name)) {
      // This environment has a local value, set it
      setLocal(name, value)
    } else {
      // If we have a parent, reach into it to set (recursing upward). If
      // we don't then blow up with an UndefinedValue
      parent.map({ p => p.set(name, value) }).getOrElse(
         throw new MoeRuntime.Errors.UndefinedValue(name)
      )
    }
  }

  private def getLocal(name: String): MoeObject = pad(name)
  private def hasLocal(name: String): Boolean   = pad.contains(name)
  private def setLocal(name: String, value: MoeObject): Unit = pad += (name -> value)

}
