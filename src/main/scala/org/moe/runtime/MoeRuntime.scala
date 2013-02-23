package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeRuntime (
    private val system: MoeSystem = new MoeSystem(),
    private val warnings: Boolean = true
  ) {

  private val VERSION         = "0.0.0"
  private val AUTHORITY       = "cpan:STEVAN"

  private var is_bootstrapped = false

  private val rootEnv     = new MoeEnvironment()
  private val rootPackage = new MoePackage("*", rootEnv)
  private val corePackage = new MoePackage("CORE", new MoeEnvironment(Some(rootEnv)))

  private val objectClass = new MoeClass("Object", Some(VERSION), Some(AUTHORITY))
  private val classClass  = new MoeClass("Class", Some(VERSION), Some(AUTHORITY), Some(objectClass))

  def isBootstrapped     = is_bootstrapped
  def areWarningsEnabled = warnings

  def getVersion     = VERSION
  def getAuthority   = AUTHORITY

  def getSystem      = system
  def getRootEnv     = rootEnv
  def getRootPackage = rootPackage
  def getCorePackage = corePackage

  def getObjectClass = objectClass
  def getClassClass  = classClass

  def bootstrap(): Unit = {
    if (!is_bootstrapped) {

      // setup the root package
      rootEnv.setCurrentPackage(rootPackage) // bind it to env

      // set up the core package
      rootPackage.addSubPackage(corePackage) // bind it to the root
      corePackage.getEnv.setCurrentPackage(corePackage) // bind it to the env

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
      val ioClass        = new MoeClass("IO",         Some(VERSION), Some(AUTHORITY), Some(anyClass))

      val undefClass     = new MoeClass("Undef",      Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val boolClass      = new MoeClass("Bool",       Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val strClass       = new MoeClass("Str",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val intClass       = new MoeClass("Int",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val numClass       = new MoeClass("Num",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val exceptionClass = new MoeClass("Exception",  Some(VERSION), Some(AUTHORITY), Some(scalarClass))

      // add all these classes to the corePackage
      corePackage addClass objectClass
      corePackage addClass classClass

      corePackage addClass anyClass
      corePackage addClass scalarClass
      corePackage addClass arrayClass
      corePackage addClass hashClass
      corePackage addClass pairClass
      corePackage addClass ioClass

      corePackage addClass undefClass
      corePackage addClass boolClass
      corePackage addClass strClass
      corePackage addClass intClass
      corePackage addClass numClass
      corePackage addClass exceptionClass

      setupBuiltins

      /*
        TODO:
        - bootstrap the other associtateClass for MoeClass, MoeAttribute,MoeMethod, etc.
        - wrap STDIN, STDOUT, STDERR from MoeSystem in an IO class before they are exposed
      */

      is_bootstrapped = true
    }
  }

  def getCoreClassFor (name: String): Option[MoeClass] = corePackage getClass name

  def warn(msg: String): Unit = warn(Array(msg))
  def warn(msg: Array[String]): Unit = {
    val out = msg.mkString
    // TODO: 
    // add line numbers and such 
    // to the message, when we have
    // actual line numbers that is
    // - SL
    system.getSTDERR println out
  }

  def print(msg: String): Unit = print(Array(msg))
  def print(msg: Array[String]): Unit = system.getSTDOUT print msg.mkString

  def say(msg: String): Unit = say(Array(msg))
  def say(msg: Array[String]): Unit = system.getSTDOUT println msg.mkString

  private def setupBuiltins = {
    import org.moe.runtime.builtins._

    ClassClass(this)
    ObjectClass(this)

    AnyClass(this)
    ScalarClass(this)
    ArrayClass(this)
    HashClass(this)
    PairClass(this)
    IOClass(this)

    UndefClass(this)
    BoolClass(this)
    StrClass(this)
    IntClass(this)
    NumClass(this)
    ExceptionClass(this)
  }

  object NativeObjects {
    import org.moe.runtime.nativeobjects._

    private lazy val Undef = new MoeUndefObject(getCoreClassFor("Undef"))
    private lazy val True  = new MoeBoolObject(true, getCoreClassFor("Bool"))
    private lazy val False = new MoeBoolObject(false, getCoreClassFor("Bool"))

    def getUndef = Undef
    def getTrue  = True
    def getFalse = False

    def getInt  (value: Int)     = new MoeIntObject(value, getCoreClassFor("Int"))
    def getNum  (value: Double)  = new MoeNumObject(value, getCoreClassFor("Num"))
    def getStr  (value: String)  = new MoeStrObject(value, getCoreClassFor("Str"))
    def getBool (value: Boolean) = if (value) { True } else { False }

    def getHash  (value: HashMap[String, MoeObject]) = new MoeHashObject(value, getCoreClassFor("Hash"))
    def getHash  ()                                  = new MoeHashObject(HashMap(), getCoreClassFor("Hash"))
    def getArray (value: List[MoeObject])            = new MoeArrayObject(value, getCoreClassFor("Array"))
    def getArray ()                                  = new MoeArrayObject(List(), getCoreClassFor("Array"))
    def getPair  (value: (MoeObject, MoeObject))     = new MoePairObject(
      (value._1.unboxToString.get, value._2),
      getCoreClassFor("Pair")
    )

  }

}
