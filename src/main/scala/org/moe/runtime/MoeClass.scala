package org.moe.runtime

import scala.collection.mutable.{HashMap,Map}

class MoeClass(
    private val name: String,
    private var version: Option[String] = None,
    private var authority: Option[String] = None,
    private var superclass: Option[MoeClass] = None
  ) extends MoeObject {

  private val methods: Map[String,MoeMethod] = new HashMap[String, MoeMethod]()
  private val attributes: Map[String,MoeAttribute] = new HashMap[String, MoeAttribute]()

  // Identity ...

  /**
   * Returns the name of this class
   */
  def getName: String = name

  /**
   * Returns the version of this class
   */
  def getVersion: Option[String] = version

  /**
   * Returns the authority of this class
   */
  def getAuthority: Option[String] = authority

  /**
   * Sets the version of this class
   *
   * @param v The version
   */
  def setVersion(v: Option[String]) = version = v

  /**
   * Sets the authority of this class
   *
   * @param a The authority
   */
  def setAuthority(a: Option[String]) = authority = a

  // Superclass ...

  /**
   * Returns this superclass of this class
   */
  def getSuperclass: Option[MoeClass] = superclass

  /**
   * Returns true if this class has a superclass
   */
  def hasSuperclass: Boolean  = superclass.isDefined

  /**
   * Sets the superclass of this class.
   */
  def setSuperclass(s: Option[MoeClass]) = superclass = s

  /**
   * Gets a list of classes in method resolution order for this class.
   */
  def getMRO: List[MoeClass] = {
    superclass match {
      case Some(s) => this :: s.getMRO
      case None    => List(this)
    }
  }

  // Attributes

  /**
   * Adds an attribute to this class
   */
  def addAttribute(attribute: MoeAttribute): Unit = {
    attributes += (attribute.getName -> attribute)
  }

  /**
   * Returns this class' attribute with the specified name.
   *
   * @param name The name of the attribute to return
   */
  def getAttribute(name: String): MoeAttribute = {
    // If, in the future, it is decided that this method should just return
    // Option[MoeAttribute] then the assignment to a and subsequent definedness
    // check can go away and the superclass.map can be adjusted - @gphat
    val a = attributes.get(name).orElse(
      superclass.map({ sc => sc.getAttribute(name) })
    )

    if(!a.isDefined) {
      throw new Runtime.Errors.AttributeNotFound(name)
    }

    a.get
  }

  /**
   * Returns true if this class (or any of it's superclasses) has an attribute
   * with the specified name.
   *
   * @param name The name of the attribute to check for
   */
  def hasAttribute(name: String): Boolean = {

    // If getAttribute returned Option[MoeAttribute], this would become
    // getMethod(name).isDefined - @gphat
    try {
      getAttribute(name)
      true
    } catch {
      case e: Exception => false
    }
  }

  /**
   * Returns a [[scala.collection.Map]] of names and attributes for this class
   * and all of it's superclasses.
   */
  private def collectAllAttributes: Map[String, MoeAttribute] = {
    superclass match {
      case None    => attributes.clone
      case Some(s) => s.collectAllAttributes ++ attributes
    }
  }

  // Instances

  /**
   * Creates a new instance of this class.
   */
  def newInstance: MoeObject = {
    val instance = new MoeOpaque(this)
    collectAllAttributes.values.foreach(
      (attr) => instance.setValue(attr.getName, null)
    )
    instance
  }

  // Methods ...

  /**
   * Adds a method to this class.
   *
   * @param method The method to add to this class
   */
  def addMethod(method: MoeMethod): Unit = {
    methods += (method.getName -> method)
  }

  /**
   * Returns this class' method with the specified name.
   *
   * @param name The name of the method to return
   */
  def getMethod(name: String): MoeMethod = {

    // If, in the future, it is decided that this method should just return
    // Option[MoeMethod] then the assignment to m and subsequent definedness
    // check can go away and the superclass.map can be adjusted - @gphat
    val m = methods.get(name).orElse(
      superclass.map({ sc => sc.getMethod(name) })
    )

    if(!m.isDefined) {
      throw new Runtime.Errors.MethodNotFound(name)
    }

    m.get
  }

  /**
   * Returns true if this class has a method with the specified name.
   *
   * @param name The name of the method to check for.
   */
  def hasMethod(name: String): Boolean = {

    // If getMethod returned Option[MoeMethod], this would become
    // getMethod(name).isDefined - @gphat
    try {
        getMethod(name)
        true
    } catch {
      case e: Exception => false
    }
  }

  // Utils ...

  /**
   * Returns a string representation of this class.
   */
  override def toString: String = {
    "{ " + name + "-" + version.getOrElse("") + "-" + authority.getOrElse("") + superclass.map({ s =>
      " #extends " + s.toString
    }).getOrElse("") + "}"
  }

}