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

  private val unknownClass  = new MoeClass("Unknown", Some(VERSION), Some(AUTHORITY), Some(objectClass))

  private val classInstance  = classClass.newInstance
  private val objectInstance  = objectClass.newInstance
  private val unknownInstance  = unknownClass.newInstance

  def getVersion     = VERSION
  def getAuthority   = AUTHORITY

  def isBootstrapped = is_bootstrapped

  def getSystem      = system
  def getRootEnv     = rootEnv
  def getRootPackage = rootPackage
  def getCorePackage = corePackage

  def getObjectClass = objectClass
  def getClassClass  = classClass

  def getObjectInstance = objectInstance
  def getClassInstance  = classInstance
  def getUnknownInstance  = unknownInstance

  implicit def unboxClass(c: Option[MoeClass]) = c.getOrElse(unknownClass)
  implicit def unboxInstance(c: Option[MoeOpaque]) = c.getOrElse(unknownInstance)

  def bootstrap() : Unit = {
    if (!is_bootstrapped) {

      //val unboxInstance: (Option[MoeOpaque]) => MoeOpaque = { _.getOrElse(unknownInstance) }
      //val unboxClass: (Option[MoeClass]) => MoeClass = { _.getOrElse(unknownClass) }

      // setup the root package
      rootEnv.setCurrentPackage(rootPackage) // bind it to env

      // set up the core package
      rootPackage.addSubPackage(corePackage) // bind it to the root
      corePackage.getEnv.setCurrentPackage(corePackage) // bind it to the env

      // bootstrap builtin methods
      classClass.addMethod(
        new MoeMethod(
          "new",
          { (invocant, _) => invocant.getAssociatedClass.newInstance }
        )
      )
      classClass.addMethod(
        new MoeMethod(
          "getClass", // XXX
          { (invocant, _) => invocant.getAssociatedClass }
        )
      )

      // tie the knot
      objectClass.setAssociatedClass(Some(classClass)) // Object is a class
      classClass.setAssociatedClass(Some(classClass))  // Class is a class

      /**
       * NOTE:
       * These are the core classes in our runtime. 
       * They are meant to look like the Perl 6 types
       * but ultimately will be for Perl 5 so there 
       * will be some variance here and there.
       * - SL
       *
       * SEE ALSO: 
       *  - http://perlcabal.org/syn/S02.html
       *  - https://raw.github.com/lue/Perl-6-Type-Hierarchy/master/notes/S02.pod6
       */

      val anyClass       = new MoeClass("Any",        Some(VERSION), Some(AUTHORITY), Some(objectClass))

      val scalarClass    = new MoeClass("Scalar",     Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val arrayClass     = new MoeClass("Array",      Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val hashClass      = new MoeClass("Hash",       Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val pairClass      = new MoeClass("Pair",       Some(VERSION), Some(AUTHORITY), Some(anyClass))

      val nullClass      = new MoeClass("Null",       Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val boolClass      = new MoeClass("Bool",       Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val strClass       = new MoeClass("Str",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val intClass       = new MoeClass("Int",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val numClass       = new MoeClass("Num",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val exceptionClass = new MoeClass("Exception",  Some(VERSION), Some(AUTHORITY), Some(scalarClass))

      // add all these classes to the corePackage
      corePackage.addClass(objectClass.newInstance)
      corePackage.addClass(classClass.newInstance)

      corePackage.addClass(anyClass.newInstance)
      corePackage.addClass(scalarClass.newInstance)
      corePackage.addClass(arrayClass.newInstance)
      corePackage.addClass(hashClass.newInstance)
      corePackage.addClass(pairClass.newInstance)

      corePackage.addClass(nullClass.newInstance)
      corePackage.addClass(boolClass.newInstance)
      corePackage.addClass(strClass.newInstance)
      corePackage.addClass(intClass.newInstance)
      corePackage.addClass(numClass.newInstance)
      corePackage.addClass(exceptionClass.newInstance)

      addBuiltinMethods

      /*
        TODO:
        bootstrap the other associtateClass for 
        MoeClass, MoeAttribute,MoeMethod, etc.
      */

      is_bootstrapped = true
    }
  }

  private def addBuiltinMethods = {
    val associatedClassFor: (String) => MoeClass =
      ensureCoreClassFor(_).getAssociatedClass.getOrElse(unknownClass)

    val int_class = associatedClassFor("Int")
    int_class.addMethod(
      new MoeMethod(
        "+",
        { (lhs, args) =>
          val rhs = args(0)
          val i = lhs.asInstanceOf[MoeIntObject]
          rhs match {
            case rhs_i: MoeIntObject => new MoeIntObject(i.getNativeValue + rhs_i.getNativeValue)
            case rhs_n: MoeFloatObject => new MoeFloatObject(i.getNativeValue + rhs_n.getNativeValue.toInt)
            case _ => throw new MoeErrors.UnexpectedType(rhs.toString)
          }
        }
      )
    )
  }

  def getCoreClassFor (name: String): Option[MoeOpaque] = corePackage.getClass(name)
  def ensureCoreClassFor (name: String): MoeOpaque = corePackage.getClass(name).getOrElse(unknownClass.newInstance)

  object NativeObjects {

    private lazy val Undef = new MoeNullObject(ensureCoreClassFor("Null").getAssociatedClass)
    private lazy val True  = new MoeBooleanObject(true, ensureCoreClassFor("Bool").getAssociatedClass)
    private lazy val False = new MoeBooleanObject(false, ensureCoreClassFor("Bool").getAssociatedClass)

    def getUndef = Undef
    def getTrue  = True
    def getFalse = False

    def getInt    (value: Int)     = new MoeIntObject(value, ensureCoreClassFor("Int").getAssociatedClass)
    def getFloat  (value: Double)  = new MoeFloatObject(value, ensureCoreClassFor("Num").getAssociatedClass)
    def getString (value: String)  = new MoeStringObject(value, ensureCoreClassFor("Str").getAssociatedClass)
    def getBool   (value: Boolean) = if (value) { True } else { False }

    def getHash  (value: Map[String, MoeObject]) = new MoeHashObject(value, ensureCoreClassFor("Hash").getAssociatedClass)
    def getHash  ()                              = new MoeHashObject(Map(), ensureCoreClassFor("Hash").getAssociatedClass)
    def getArray (value: List[MoeObject])        = new MoeArrayObject(value, ensureCoreClassFor("Array").getAssociatedClass)
    def getArray ()                              = new MoeArrayObject(List(), ensureCoreClassFor("Array").getAssociatedClass)
    def getPair  (value: (MoeObject, MoeObject)) = new MoePairObject(
        (value._1.asInstanceOf[MoeStringObject].getNativeValue, value._2),
        ensureCoreClassFor("Pair").getAssociatedClass
      )
  }

}
