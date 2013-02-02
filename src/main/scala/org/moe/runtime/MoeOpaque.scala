package org.moe.runtime

import scala.collection.mutable.{Map,HashMap}

class MoeOpaque(
    private var associatedClass: Option[MoeClass] = None
  ) extends MoeObject(associatedClass) {

  private val data: Map[String, MoeObject] = new HashMap[String, MoeObject]()

  def hasValue(name: String): Boolean                = data.contains(name)
  def getValue(name: String): Option[MoeObject]      = data.get(name)
  def setValue(name: String, value: MoeObject): Unit = data.put(name, value)

}