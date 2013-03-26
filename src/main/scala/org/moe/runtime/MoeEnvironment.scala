package org.moe.runtime

import scala.collection.mutable.{Map,HashMap}

import org.moe.runtime._

/**
 * MoeEnvironment: Presumably the moe environment
 *
 * @param parent optional parent environment
 */

class MoeEnvironment(
    private val parent: Option[MoeEnvironment] = None
  ) {

  object Markers {
    val Runtime  = "$?RUNTIME"
    val Package  = "$?PACKAGE"
    val Class    = "$?CLASS"
    val Invocant = "$?SELF"
    val Topic    = "$_"
  }

  private val pad: Map[String, MoeObject] = new HashMap[String, MoeObject]()

  def getCurrentRuntime  : Option[MoeRuntime] = get(Markers.Runtime).asInstanceOf[Option[MoeRuntime]]
  def getCurrentPackage  : Option[MoePackage] = get(Markers.Package).asInstanceOf[Option[MoePackage]]
  def getCurrentClass    : Option[MoeClass]   = get(Markers.Class).asInstanceOf[Option[MoeClass]]
  def getCurrentInvocant : Option[MoeObject]  = get(Markers.Invocant)
  def getCurrentTopic    : Option[MoeObject]  = get(Markers.Topic)

  def setCurrentRuntime  (p: MoeRuntime): Unit = setLocal(Markers.Runtime,  p.asInstanceOf[MoeObject])
  def setCurrentPackage  (p: MoePackage): Unit = setLocal(Markers.Package,  p.asInstanceOf[MoeObject])
  def setCurrentClass    (c: MoeClass  ): Unit = setLocal(Markers.Class,    c.asInstanceOf[MoeObject])
  def setCurrentInvocant (i: MoeObject ): Unit = setLocal(Markers.Invocant, i)
  def setCurrentTopic    (t: MoeObject ): Unit = setLocal(Markers.Topic,    t)

  def getParent = parent
  def isRoot    = !parent.isDefined

  def get (name: String): Option[MoeObject] = {
    if (hasLocal(name)) return getLocal(name)
    parent.flatMap( _.get(name) )
  }

  def has (name: String): Boolean = {
    if (hasLocal(name)) return true
    parent.exists( _.has(name) )
  }

  def create (name: String, value: MoeObject): Option[MoeObject] = setLocal(name, value)

  def set (name: String, value: MoeObject): Option[MoeObject] = {
    if (!has(name)) return None
    if (hasLocal(name)) {
      setLocal(name, value)
    } else {
      parent.flatMap( _.set(name, value) )
    }
  }

  private def getLocal(name: String): Option[MoeObject] = pad.get(name)
  private def hasLocal(name: String): Boolean           = pad.contains(name)
  private def setLocal(name: String, value: MoeObject): Option[MoeObject] = {
    pad += (name -> value)
    Some(value)
  }

}
