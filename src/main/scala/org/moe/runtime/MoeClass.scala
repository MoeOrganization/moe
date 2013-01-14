package org.moe.runtime

import scala.collection.mutable.{HashMap,Map}

class MoeClass (private val name: String) extends MoeObject {

  private var version: String = _
  private var authority: String = _
  private var superclass: Option[MoeClass] = None

  private val methods: Map[String,MoeMethod] = new HashMap[String, MoeMethod]()
  private val attributes: Map[String,MoeAttribute] = new HashMap[String, MoeAttribute]()

  // the various alternate constructors ...

  def this(name: String, superclass: Option[MoeClass]) = {
    this(name)
    setSuperclass(superclass)
  }

  def this(name: String, version: String) = {
    this(name)
    setVersion(version)
  }

  def this(name: String, version: String, superclass: Option[MoeClass]) = {
    this(name, version)
    setSuperclass(superclass)
  }

  def this(name: String, version: String, authority: String) = {
    this(name, version)
    setAuthority(authority)
  }

  def this(name: String, version: String, authority: String, superclass: Option[MoeClass]) = {
    this(name, version, authority)
    setSuperclass(superclass)
  }

  // Identity ...

  def getName: String = name
  def getVersion: String = version
  def getAuthority: String = authority

  def setVersion(v: String): Unit = version = v
  def setAuthority(a: String): Unit = authority = a

  // Superclass ...

  def getSuperclass: Option[MoeClass] = superclass
  def hasSuperclass: Boolean  = superclass.isDefined
  def setSuperclass(s: Option[MoeClass]) = superclass = s

  def getMRO: List[MoeClass] = {
    superclass.map(s => this:: s.getMRO).getOrElse(List(this))
  }

  // Attributes

  def addAttribute(attribute: MoeAttribute): Unit = {
    attributes += (attribute.getName -> attribute)
  }

  def getAttribute(name: String): MoeAttribute = {
    if (attributes.contains(name)) return attributes(name)
    if (hasSuperclass) return superclass.get.getAttribute(name)
    throw new Runtime.Errors.AttributeNotFound(name)
  }

  def hasAttribute(name: String): Boolean = {
    if (attributes.contains(name)) return true
    if (hasSuperclass) return superclass.get.hasAttribute(name)
    false
  }

  private def collectAllAttributes: Map[String, MoeAttribute] = {
    superclass.map({ s => s.collectAllAttributes ++ attributes }).getOrElse(attributes.clone)
  }

  // Instances

  def newInstance: MoeObject = {
    val instance = new MoeOpaque(this)
    collectAllAttributes.values.foreach(
      (attr) => instance.setValue(attr.getName, null)
    )
    instance
  }

  // Methods ...

  def addMethod(method: MoeMethod): Unit = {
    methods += (method.getName -> method)
  }

  def getMethod(name: String): MoeMethod = {
    if (methods.contains(name)) return methods(name)
    if (hasSuperclass) return superclass.get.getMethod(name)
    throw new Runtime.Errors.MethodNotFound(name)
  }

  def hasMethod(name: String): Boolean = {
    if (methods.contains(name)) return true
    if (hasSuperclass) return superclass.get.hasMethod(name)
    false
  }

  // Utils ...

  override def toString: String = {
    "{ " + name + "-" + version + "-" + authority + superclass.map({ s =>
      " #extends " + s.toString
    }).getOrElse("") + "}"
  }

}