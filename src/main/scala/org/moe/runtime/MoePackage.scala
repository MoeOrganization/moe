package org.moe.runtime

import scala.collection.mutable.{HashMap,Map}

object MoePackage {

  def findPackageByName (name: String, pkg: MoePackage): Option[MoePackage] = {
    name.split("::").foldLeft[Option[MoePackage]](Some(pkg))(
      (parent, sub) => parent.get.getSubPackage(sub)
    )
  }

  def deconstructNamespace(full: String): (Option[String], String) = {
    val split_name = full.split("::")
    if (split_name.length == 1) 
      (None -> split_name.last)
    else   
      (Some(split_name.dropRight(1).mkString("::")) -> split_name.last)
  }
}

/**
 * A Package!
 *
 * @param name The name of the package
 * @param env The package's environment
 * @param parent The package's parent package
 */
class MoePackage(
    private val name: String,
    private var env: MoeEnvironment
  ) extends MoeObject {

  private var parent: Option[MoePackage] = None
  private val klasses: Map[String, MoeClass] = new HashMap[String, MoeClass]()
  private val subs: Map[String, MoeSubroutine] = new HashMap[String, MoeSubroutine]()
  private val sub_packages: Map[String, MoePackage] = new HashMap[String, MoePackage]()

  /**
   * returns the name of this package
   */
  def getName: String = name

  /**
   * returns the local package environment
   */
  def getEnv: MoeEnvironment = env

  /**
   * returns true if this package has a parent
   */
  def isRoot: Boolean = !parent.isDefined

  // Parent ...

  /**
   * returns the parent of this package
   */
  def getParent: Option[MoePackage] = parent

  /**
   * sets the parent of this package
   *
   * @param p The package
   */
  private def setParent(p: Option[MoePackage]): Unit = parent = p

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
   */
  def getSubroutine(name: String): Option[MoeSubroutine] = subs.get(name)

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
   */
  def getClass(name: String): Option[MoeClass] = klasses.get(name)

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
    pkg.attachToParent(this)
    sub_packages += (pkg.getName -> pkg)
  }

  private def attachToParent(parent: MoePackage): Unit = setParent(Some(parent))

  /**
   * returns the SubPackaged with the specified name
   *
   * @param name the name of the SubPackage to return
   */
  def getSubPackage(name: String): Option[MoePackage] = sub_packages.get(name)

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
