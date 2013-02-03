package org.moe.runtime

class MoeRuntime (
    private val system: MoeSystem = new MoeSystem()
  ) {

  private val VERSION         = "0.0.0"
  private val AUTHORITY       = "cpan:STEVAN"

  private var is_bootstrapped = false

  private val rootEnv     = new MoeEnvironment()
  private val rootPackage = new MoePackage("*", rootEnv)
  private val corePackage = new MoePackage("CORE", new MoeEnvironment(Some(rootEnv)))

  private val objectClass = new MoeClass("Object", Some(VERSION), Some(AUTHORITY))
  private val classClass  = new MoeClass("Class", Some(VERSION), Some(AUTHORITY), Some(objectClass))

  def getVersion     = VERSION
  def getAuthority   = AUTHORITY

  def isBootstrapped = is_bootstrapped

  def getSystem      = system
  def getRootEnv     = rootEnv
  def getRootPackage = rootPackage
  def getCorePackage = corePackage

  def getObjectClass = objectClass
  def getClassClass  = classClass

  def bootstrap() : Unit = {
    if (!is_bootstrapped) {

      // setup the root package
      rootEnv.setCurrentPackage(rootPackage) // bind it to env

      // set up the core package
      rootPackage.addSubPackage(corePackage) // bind it to the root
      corePackage.getEnv.setCurrentPackage(corePackage) // bind it to the env

      // tie the knot
      objectClass.setAssociatedClass(Some(classClass)) // Object is a class
      classClass.setAssociatedClass(Some(classClass))  // Class is a class

      /*
        TODO:
        bootstrap the other associtateClass for 
        MoeClass, MoeAttribute,MoeMethod, etc.
      */

      // create the core classes
      /* 
        NOTE:
        Do some research on the core Perl 6
        classes, we should be following their
        lead (within reason)
      */
      val scalarClass = new MoeClass("Scalar", Some(VERSION), Some(AUTHORITY), Some(objectClass))
      val arrayClass  = new MoeClass("Array",  Some(VERSION), Some(AUTHORITY), Some(objectClass))
      val hashClass   = new MoeClass("Hash",   Some(VERSION), Some(AUTHORITY), Some(objectClass))
      val pairClass   = new MoeClass("Pair",   Some(VERSION), Some(AUTHORITY), Some(objectClass))

      corePackage.addClass(scalarClass)
      corePackage.addClass(arrayClass)
      corePackage.addClass(hashClass)
      corePackage.addClass(pairClass)

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

  def getCoreClassFor (name: String): Option[MoeClass] = corePackage.getClass(name)

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
    def getBool   (value: Boolean) = if (value) { True } else { False }

    def getHash  (value: Map[String, MoeObject]) = new MoeHashObject(value, getCoreClassFor("Hash"))
    def getHash  ()                              = new MoeHashObject(Map(), getCoreClassFor("Hash"))
    def getArray (value: List[MoeObject])        = new MoeArrayObject(value, getCoreClassFor("Array"))
    def getArray ()                              = new MoeArrayObject(List(), getCoreClassFor("Array"))
    def getPair  (value: (MoeObject, MoeObject)) = new MoePairObject(
        (value._1.asInstanceOf[MoeStringObject].getNativeValue, value._2), 
        getCoreClassFor("Pair")
      )
  }

}