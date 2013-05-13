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

  private object Markers {
    val Runtime   = "$?RUNTIME"
    val Package   = "$?PACKAGE"
    val Class     = "$?CLASS"
    val Invocant  = "$?SELF"
    val Topic     = "$_"
    val Exception = "$!"
  }

  def isSpecialMarker(m: String) =
    m == Markers.Runtime || m == Markers.Package  ||
    m == Markers.Class   || m == Markers.Invocant ||
    m == Markers.Topic   || m == Markers.Exception

  private val pad: Map[String, MoeObject] = new HashMap[String, MoeObject]()

  def getCurrentRuntime   : Option[MoeRuntime] = getAs[MoeRuntime](Markers.Runtime)
  def getCurrentPackage   : Option[MoePackage] = getAs[MoePackage](Markers.Package)
  def getCurrentClass     : Option[MoeClass]   = getAs[MoeClass](Markers.Class)
  def getCurrentInvocant  : Option[MoeObject]  = get(Markers.Invocant)
  def getCurrentTopic     : Option[MoeObject]  = get(Markers.Topic)
  def getCurrentException : Option[MoeObject]  = get(Markers.Exception) 

  def setCurrentRuntime   (p: MoeRuntime): Unit = setLocal(Markers.Runtime,   p.asInstanceOf[MoeObject])
  def setCurrentPackage   (p: MoePackage): Unit = setLocal(Markers.Package,   p.asInstanceOf[MoeObject])
  def setCurrentClass     (c: MoeClass  ): Unit = setLocal(Markers.Class,     c.asInstanceOf[MoeObject])
  def setCurrentInvocant  (i: MoeObject ): Unit = setLocal(Markers.Invocant,  i)
  def setCurrentTopic     (t: MoeObject ): Unit = setLocal(Markers.Topic,     t)
  def setCurrentException (e: MoeObject ): Unit = setLocal(Markers.Exception, e)

  def clearCurrentRuntime   : Unit = clearLocal(Markers.Runtime)
  def clearCurrentPackage   : Unit = clearLocal(Markers.Package)
  def clearCurrentClass     : Unit = clearLocal(Markers.Class)
  def clearCurrentInvocant  : Unit = clearLocal(Markers.Invocant)
  def clearCurrentTopic     : Unit = clearLocal(Markers.Topic)
  def clearCurrentException : Unit = clearLocal(Markers.Exception)

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

  // *As[T] versions

  def getAs[T](name: String): Option[T] = {
    if (hasLocal(name)) return getLocal(name).map(x => x.asInstanceOf[T])
    parent.flatMap( _.getAs[T](name) )
  }

  def getCurrentInvocantAs [T]: Option[T] = getAs[T](Markers.Invocant)
  def getCurrentTopicAs    [T]: Option[T] = getAs[T](Markers.Topic)

  // private ...

  private def getLocal(name: String): Option[MoeObject] = pad.get(name)
  private def hasLocal(name: String): Boolean           = pad.contains(name)
  private def clearLocal(name: String): Unit            = pad.remove(name)
  private def setLocal(name: String, value: MoeObject): Option[MoeObject] = {
    pad += (name -> value)
    Some(value)
  }

  override def toString = pad.toString + "\n" + parent.map(_.toString)
}
