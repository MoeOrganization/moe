package org.moe.runtime

object MoeRuntime {

  private val VERSION         = "0.0.0"
  private val AUTHORITY       = "cpan:STEVAN"

  private var is_bootstrapped = false

  private val rootEnv         = new MoeEnvironment()
  private val rootPackage     = new MoePackage("*", rootEnv)
  private val corePackage     = new MoePackage("CORE", new MoeEnvironment(Some(rootEnv)), Some(rootPackage))

  private val objectClass     = new MoeClass("Object", Some(VERSION), Some(AUTHORITY))
  private val classClass      = new MoeClass("Class", Some(VERSION), Some(AUTHORITY), Some(objectClass))

  def getVersion     = VERSION
  def getAuthority   = AUTHORITY

  def isBootstrapped = is_bootstrapped

  def getRootEnv     = rootEnv
  def getRootPackage = rootPackage
  def getCorePackage = corePackage

  def getObjectClass = objectClass
  def getClassClass  = classClass

  def getCoreClassFor (name: String): Option[MoeClass] = corePackage.getClass(name)

  def bootstrap (): Unit = {
    if (!is_bootstrapped) {

      objectClass.setAssociatedClass(Some(classClass)) // Object is a class
      classClass.setAssociatedClass(Some(classClass))  // Class is a class

      val scalarClass = new MoeClass("Scalar", Some(VERSION), Some(AUTHORITY), Some(objectClass))
      val arrayClass  = new MoeClass("Array",  Some(VERSION), Some(AUTHORITY), Some(objectClass))
      val hashClass   = new MoeClass("Hash",   Some(VERSION), Some(AUTHORITY), Some(objectClass))

      corePackage.addClass(scalarClass)
      corePackage.addClass(arrayClass)
      corePackage.addClass(hashClass)

      val nullClass      = new MoeClass("Null",      Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val booleanClass   = new MoeClass("Boolean",   Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val exceptionClass = new MoeClass("Exception", Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val stringClass    = new MoeClass("String",    Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val numberClass    = new MoeClass("Number",    Some(VERSION), Some(AUTHORITY), Some(scalarClass))

      corePackage.addClass(nullClass)
      corePackage.addClass(booleanClass)
      corePackage.addClass(exceptionClass)
      corePackage.addClass(stringClass)
      corePackage.addClass(numberClass)

      is_bootstrapped = true
    }
  }

  object NativeObjects {

    private lazy val Undef = new MoeNullObject(getCoreClassFor("Null"))
    private lazy val True  = new MoeBooleanObject(true, getCoreClassFor("Boolean"))
    private lazy val False = new MoeBooleanObject(false, getCoreClassFor("Boolean"))

    def getUndef = Undef
    def getTrue  = True
    def getFalse = False

    def getInt    (value: Int)     = new MoeIntObject(value, getCoreClassFor("Number"))
    def getFloat  (value: Double)  = new MoeFloatObject(value, getCoreClassFor("Number"))
    def getString (value: String)  = new MoeStringObject(value, getCoreClassFor("String"))
    def getBool   (value: Boolean) = new MoeBooleanObject(value, getCoreClassFor("Boolean"))

    def getPair  (value: (MoeObject, MoeObject)) = new MoePairObject((value._1.asInstanceOf[MoeStringObject].getNativeValue, value._2))
    def getHash  (value: Map[String, MoeObject]) = new MoeHashObject(value, getCoreClassFor("Hash"))
    def getArray (value: List[MoeObject])        = new MoeArrayObject(value, getCoreClassFor("Array"))
  }

  /**
   * TODO:
   * Need to hook up these classes with
   * their runtime counterparts, when
   * we actually have them that is.
   * - SL
   */
  object Errors {
    class MoeException          (msg: String) extends Exception(msg)
    class MoeMoney              (msg: String) extends MoeException(msg)

    // MoeProblems is derived from MoeMoney - RIP B.I.G
    class MoeProblems           (msg: String) extends MoeMoney(msg)

    class NotAllowed            (msg: String) extends MoeProblems(msg)
    class MethodNotAllowed      (msg: String) extends NotAllowed(msg)

    class ValueNotFound         (msg: String) extends MoeProblems(msg)
    class UnknownNode           (msg: String) extends MoeProblems(msg)
    class PackageNotFound       (msg: String) extends ValueNotFound(msg)
    class InstanceValueNotFound (msg: String) extends ValueNotFound(msg)
    class ClassNotFound         (msg: String) extends ValueNotFound(msg)
    class SuperclassNotFound    (msg: String) extends ValueNotFound(msg)
    class MethodNotFound        (msg: String) extends ValueNotFound(msg)
    class AttributeNotFound     (msg: String) extends ValueNotFound(msg)
    class SubroutineNotFound    (msg: String) extends ValueNotFound(msg)
    class VariableNotFound      (msg: String) extends ValueNotFound(msg)

    class UndefinedValue        (msg: String) extends MoeProblems(msg)
    class UndefinedMethod       (msg: String) extends UndefinedValue(msg)
    class UndefinedSubroutine   (msg: String) extends UndefinedValue(msg)

    class MissingValue          (msg: String) extends MoeProblems(msg)
    class MissingClass          (msg: String) extends MissingValue(msg)
  }

}