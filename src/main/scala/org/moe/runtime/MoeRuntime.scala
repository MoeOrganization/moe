package org.moe.runtime

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
       
      val undefClass     = new MoeClass("Undef",      Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val boolClass      = new MoeClass("Bool",       Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val strClass       = new MoeClass("Str",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val intClass       = new MoeClass("Int",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val numClass       = new MoeClass("Num",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val exceptionClass = new MoeClass("Exception",  Some(VERSION), Some(AUTHORITY), Some(scalarClass))

      // add all these classes to the corePackage
      corePackage.addClass(objectClass)
      corePackage.addClass(classClass)

      corePackage.addClass(anyClass)
      corePackage.addClass(scalarClass)
      corePackage.addClass(arrayClass)
      corePackage.addClass(hashClass)
      corePackage.addClass(pairClass)

      corePackage.addClass(undefClass)
      corePackage.addClass(boolClass)
      corePackage.addClass(strClass)
      corePackage.addClass(intClass)
      corePackage.addClass(numClass)
      corePackage.addClass(exceptionClass)

      setupBuiltins

      /*
        TODO:
        bootstrap the other associtateClass for 
        MoeClass, MoeAttribute,MoeMethod, etc.
      */

      is_bootstrapped = true
    }
  }

  def getCoreClassFor (name: String): Option[MoeClass] = corePackage.getClass(name)

  def warn(msg: String): Unit = warn(Array(msg))
  def warn(msg: Array[String]): Unit = {
    val out = msg.mkString
    // TODO: 
    // add line numbers and such 
    // to the message, when we have
    // actual line numbers that is
    // - SL
    system.getSTDERR.println(out)
  }

  def print(msg: String): Unit = print(Array(msg))
  def print(msg: Array[String]): Unit = system.getSTDOUT.print(msg.mkString)

  def say(msg: String): Unit = say(Array(msg))
  def say(msg: Array[String]): Unit = system.getSTDOUT.println(msg.mkString)

  private def setupBuiltins = {
    import org.moe.runtime.builtins._

    ClassClass(this) 
    ObjectClass(this)

    AnyClass(this)
    ScalarClass(this)
    ArrayClass(this)
    HashClass(this)
    PairClass(this)

    UndefClass(this)
    BoolClass(this)
    StrClass(this)
    IntClass(this)
    NumClass(this)
    ExceptionClass(this)
  }

  object NativeObjects {

    private lazy val Undef = new MoeUndefObject(getCoreClassFor("Undef"))
    private lazy val True  = new MoeBooleanObject(true, getCoreClassFor("Bool"))
    private lazy val False = new MoeBooleanObject(false, getCoreClassFor("Bool"))

    def getUndef = Undef
    def getTrue  = True
    def getFalse = False

    def getInt    (value: Int)     = new MoeIntObject(value, getCoreClassFor("Int"))
    def getFloat  (value: Double)  = new MoeFloatObject(value, getCoreClassFor("Num"))
    def getString (value: String)  = new MoeStringObject(value, getCoreClassFor("Str"))
    def getBool   (value: Boolean) = if (value) { True } else { False }

    def getHash  (value: Map[String, MoeObject]) = new MoeHashObject(value, getCoreClassFor("Hash"))
    def getHash  ()                              = new MoeHashObject(Map(), getCoreClassFor("Hash"))
    def getArray (value: List[MoeObject])        = new MoeArrayObject(value, getCoreClassFor("Array"))
    def getArray ()                              = new MoeArrayObject(List(), getCoreClassFor("Array"))
    def getPair  (value: (MoeObject, MoeObject)) = new MoePairObject(
      (value._1.asInstanceOf[MoeStringObject].getNativeValue, value._2), 
      getCoreClassFor("Pair")
    )

    object Coercions {

      /**
       * NOTE:
       * So I am not sure this is the right thing to do
       * but the idea below here is that if we can reliably 
       * coerce it into a sane value that is actually useful
       * then we do it, if we can't then we return None to 
       * signal that the callee has to think about this more
       * and decide what to do on their own. That may just
       * be calling .toString, or ignoring it, or whatever
       * but we won't make that call here.
       * 
       * Also might be a good idea to add polymorphic methods
       * to MoeObject (and therefore MoeNativeObject) to allow
       * better handling at the call site. Not 100% sure of 
       * that yet though, time will tell.
       *
       * Also, I bet we could do better with the type here, 
       * the MoeObject arg type is not right, it should be 
       * MoeNativeObject, but the type parameter is messing 
       * me up, I need Scala help.
       */

      def toDouble(obj: MoeObject): Option[Double] = obj match {
        case i: MoeIntObject     => Some(i.getNativeValue.toDouble)
        case f: MoeFloatObject   => Some(f.getNativeValue)
        case s: MoeStringObject  => try { Some(s.getNativeValue.toDouble) } catch { case e: Throwable => None } 
        case u: MoeUndefObject   => None
        case b: MoeBooleanObject => None
        case a: MoeArrayObject   => None
        case h: MoeHashObject    => None
        case p: MoePairObject    => None
        case _ => throw new MoeErrors.UnexpectedType("Expected MoeNativeObject[A] and got " + obj.toString)
      }

      def toInt(obj: MoeObject): Option[Int] = obj match {
        case i: MoeIntObject     => Some(i.getNativeValue)
        case f: MoeFloatObject   => Some(f.getNativeValue.toInt)
        case s: MoeStringObject  => try { Some(s.getNativeValue.toInt) } catch { case e: Throwable => None } 
        case u: MoeUndefObject   => None
        case b: MoeBooleanObject => None
        case a: MoeArrayObject   => None
        case h: MoeHashObject    => None
        case p: MoePairObject    => None
        case _ => throw new MoeErrors.UnexpectedType("Expected MoeNativeObject[A] and got " + obj.toString)
      }

      def toString(obj: MoeObject): Option[String] = obj match {
        case i: MoeIntObject     => Some(i.getNativeValue.toString)
        case f: MoeFloatObject   => Some(f.getNativeValue.toString)
        case s: MoeStringObject  => Some(s.getNativeValue)
        case u: MoeUndefObject   => None
        case b: MoeBooleanObject => None
        case a: MoeArrayObject   => None
        case h: MoeHashObject    => None
        case p: MoePairObject    => None
        case _ => throw new MoeErrors.UnexpectedType("Expected MoeNativeObject[A] and got " + obj.toString)
      }

      def toBool(obj: MoeObject): Option[Boolean] = obj match {
        case i: MoeIntObject     => Some(i.isTrue)
        case f: MoeFloatObject   => Some(f.isTrue)
        case s: MoeStringObject  => Some(s.isTrue)
        case u: MoeUndefObject   => Some(u.isTrue)
        case b: MoeBooleanObject => Some(b.isTrue)
        case a: MoeArrayObject   => Some(a.isTrue)
        case h: MoeHashObject    => Some(h.isTrue)
        case p: MoePairObject    => Some(p.isTrue)
        case _ => throw new MoeErrors.UnexpectedType("Expected MoeNativeObject[A] and got " + obj.toString) 
      }

    }

  }

}
