package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeOpaque(private var associatedClass: Option[MoeClass] = None) extends MoeObject(associatedClass) {

  private val data = new HashMap[String, MoeObject]()

  def hasValue(name: String): Boolean   = data.contains(name)
  def getValue(name: String): MoeObject = {
    if (!hasValue(name)) throw new Runtime.Errors.InstanceValueNotFound(name)
    data(name)
  }

  def setValue(name: String, value: MoeObject): Unit = data.put(name, value)

}