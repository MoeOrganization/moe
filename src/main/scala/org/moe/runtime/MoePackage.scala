package org.moe.runtime

import scala.collection.mutable.HashMap

class MoePackage(
  private val name: String,
  private var env: MoeEnvironment)
  extends MoeObject {

  private var parent: MoePackage = _

  private val klasses = new HashMap[String, MoeClass]()
  private val subs = new HashMap[String, MoeSubroutine]()
  private val sub_packages = new HashMap[String, MoePackage]()

  def this(n: String, e: MoeEnvironment, p: MoePackage) = {
    this(n, e)
    parent = p
  }

  /**
   * returns the name of this package
   */
  def getName: String  = name

  /**
   * returns true if this package has a parent
   */
  def isRoot: Boolean = parent == null 

  // Parent ...

  /**
   * returns the parent of this package
   */
  def getParent: MoePackage = parent

  /**
   * sets the parent of this package
   *
   * @param p The package 
   */
  def setParent(p: MoePackage): Unit = parent = p

  // Subroutines ...
  
  /**
   * adds subroutine to this package
   * 
   * @param sub The subroutine
   */
  def addSubroutine(sub: MoeSubroutine): Unit = {
      subs += (sub.getName -> sub)
  }
  
  /**
   * returns the subroutine with the specified name
   *
   * @param name The name of the subroutine to return
   *
   * @throws SubroutineNotFound specified name does not match any subroutine
   */
  def getSubroutine(name: String): MoeSubroutine = {
      if (hasSubroutine(name)) return subs(name)
      throw new Runtime.Errors.SubroutineNotFound(name)
  }

  /**
   * checks if a subroutine with the specified name is in package
   *
   * @param name The name of the subroutine to check for
   */
  def hasSubroutine(name: String): Boolean = {
      if (subs.contains(name)) return true
      false
  }

  // Classes ...

  /**
   * adds class to this package
   *
   * @param klass The class
   */
  def addClass(klass: MoeClass): Unit = {
      klasses += (klass.getName -> klass)
  }

  /**
   * returns the class with the specified name
   *
   * @param name The name of the class to return
   *
   * @throws ClassNotFound specified name does not match any class
   */
  def getClass(name: String): MoeClass = {
      if (hasClass(name)) return klasses(name)
      throw new Runtime.Errors.ClassNotFound(name)
  }
  
  /**
   * checks if a class with the specified name is in package
   *
   * @param name The name of the class to check for
   */ 
  def hasClass(name: String): Boolean = {
      if (klasses.contains(name)) return true
      false
  }

  // SubPackages ...

  /**
   * add SubPackage to this package
   *
   * @param pkg The SubPackage to add
   */
  def addSubPackage(pkg: MoePackage): Unit = {
      sub_packages += (pkg.getName -> pkg)
  }

  /**
   * returns the SubPackaged with the specified name
   *
   * @param name the name of the SubPackage to return
   * 
   * @throws PackageNotFound specified name does not match any SubPackage
   */
  def getSubPackage(name: String): MoePackage = {
      if (hasSubPackage(name)) return sub_packages(name)
      throw new Runtime.Errors.PackageNotFound(name)
  }

  /**
   * checks if a SubPackage wit the specified name is in this package
   *
   * @param name The name of the SubPackage to check for
   */
  def hasSubPackage(name: String): Boolean = {
      if (sub_packages.contains(name)) return true
      false
  }

}
