package org.moe.runtime

sealed abstract class MoeType (
    private val sigil: String,
    private var associatedClass: Option[MoeClass]
  ) {
  def getSigil = sigil

  def getAssociatedClass: Option[MoeClass] = associatedClass
  def hasAssociatedClass: Boolean = associatedClass.isDefined
  def setAssociatedClass (c: Option[MoeClass]): Unit = associatedClass = c
}

case class MoeScalarType (c: Option[MoeClass] = None) extends MoeType("$", c)
case class MoeArrayType  (c: Option[MoeClass] = None) extends MoeType("@", c)
case class MoeHashType   (c: Option[MoeClass] = None) extends MoeType("%", c)
case class MoeCodeType   (c: Option[MoeClass] = None) extends MoeType("&", c)
case class MoeClassType  (c: Option[MoeClass] = None) extends MoeType("^", c)

