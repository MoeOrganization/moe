package org.moe.runtime

sealed abstract class MoeType (
    private val sigil: String,
    private val name: String,
    private var associatedClass: Option[MoeClass]
  ) {

  def getName  = name
  def getSigil = sigil

  def getAssociatedClass: Option[MoeClass] = associatedClass
  def hasAssociatedClass: Boolean = associatedClass.isDefined
  def setAssociatedClass (c: Option[MoeClass]): Unit = associatedClass = c
}

case class MoeScalarType (c: Option[MoeClass] = None) extends MoeType("$", "SCALAR", c)
case class MoeArrayType  (c: Option[MoeClass] = None) extends MoeType("@", "ARRAY",  c)
case class MoeHashType   (c: Option[MoeClass] = None) extends MoeType("%", "HASH",   c)
case class MoeCodeType   (c: Option[MoeClass] = None) extends MoeType("&", "CODE",   c)
case class MoeClassType  (c: Option[MoeClass] = None) extends MoeType("^", "CLASS",  c)

