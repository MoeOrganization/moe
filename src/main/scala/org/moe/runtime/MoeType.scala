package org.moe.runtime

object MoeType {

  def checkType (n: String, o: MoeObject) : Boolean = o.getAssociatedType.exists(checkType(n, _))
  def checkType (n: String, t: MoeType)   : Boolean = 
    t.isAnyType || n.startsWith(t.getSigil) || t.getParentType.exists(checkType(n, _))
}

sealed abstract class MoeType (
    private val sigil: String,
    private val name: String,
    private var associatedClass: Option[MoeClass],
    private var parentType: Option[MoeType]
  ) {

  def getName       = name
  def getSigil      = sigil
  def getParentType = parentType

  def getAssociatedClass: Option[MoeClass] = associatedClass
  def hasAssociatedClass: Boolean = associatedClass.isDefined
  def setAssociatedClass (c: Option[MoeClass]): Unit = associatedClass = c

  def isAnyType = false
}

case class MoeTopType    (c: Option[MoeClass] = None) extends MoeType("_", "TOP",    c, None)
case class MoeAnyType    (c: Option[MoeClass] = None) extends MoeType("*", "ANY",    c, Some(MoeTopType(c))) { override def isAnyType = true }
case class MoeScalarType (c: Option[MoeClass] = None) extends MoeType("$", "SCALAR", c, Some(MoeTopType(c)))
case class MoeArrayType  (c: Option[MoeClass] = None) extends MoeType("@", "ARRAY",  c, Some(MoeScalarType(c)))
case class MoeHashType   (c: Option[MoeClass] = None) extends MoeType("%", "HASH",   c, Some(MoeScalarType(c)))
case class MoeCodeType   (c: Option[MoeClass] = None) extends MoeType("&", "CODE",   c, Some(MoeScalarType(c)))
case class MoeClassType  (c: Option[MoeClass] = None) extends MoeType("^", "CLASS",  c, Some(MoeScalarType(c)))

