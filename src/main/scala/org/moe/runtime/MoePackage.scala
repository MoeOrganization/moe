package org.moe.runtime

import scala.collection.mutable.{HashMap,Map}

object MoePackage {
  def findPackageByName (name: String, pkg: MoePackage): Option[MoePackage] = findPackageByName(name.split("::"), pkg)
  def findPackageByName (name: Array[String], pkg: MoePackage): Option[MoePackage] = {
    name.foldLeft[Option[MoePackage]](Some(pkg))(
      (parent, sub) => parent.get.getSubPackage(sub)
    )
  }

  def createPackageTreeFromName (name: String, env: MoeEnvironment, parent: MoePackage): (MoePackage, MoePackage) = createPackageTreeFromName(name.split("::"), env, parent)
  def createPackageTreeFromName (name: Array[String], env: MoeEnvironment, parent: MoePackage): (MoePackage, MoePackage) = {
    val root = if (parent.hasSubPackage(name.head)) {
      parent.getSubPackage(name.head).get 
    } else {
      new MoePackage(name.head, env)
    }
    val leaf = name.tail.foldLeft[MoePackage](root)(
      (parent, subpkg_name) => {
        if (parent.hasSubPackage(subpkg_name)) {
          parent.getSubPackage(subpkg_name).get
        } else {
          val subpkg = new MoePackage(subpkg_name, env)
          parent.addSubPackage(subpkg)
          subpkg  
        }
      }
    ) 
    (root -> leaf)
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

  /**
   * returns a List of MoeSubroutines who have the export trait
   */
  def getExportedSubroutines: List[MoeSubroutine] = {
    subs.foldLeft[List[MoeSubroutine]](List())(
      (l, p) => if (p._2.hasTrait("export")) p._2 :: l else l
    )
  }

  def importSubroutines(subs: List[MoeSubroutine]) = subs.foreach(addSubroutine(_))

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
