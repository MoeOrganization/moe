package org.moe.runtime

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class MoeRuntime (
    private val system: MoeSystem = new MoeSystem(),
    private val warnings: Boolean = true,
    private val debug: Boolean = false,
    private val interpreter: Option[MoeInterpreter] = None 
  ) extends MoeObject {

  private val VERSION         = "0.0.0"
  private val AUTHORITY       = "cpan:STEVAN"

  private var is_bootstrapped = false

  // these are global variables tied to the 
  // runtime that we will expose to the user
  // but also need to system to maintain a
  // connection to them as well
  private val systemEnv:   HashMap[String,MoeObject] = HashMap()
  private val includeDirs: ArrayBuffer[MoeObject]    = ArrayBuffer()
  
  private val rootEnv     = new MoeEnvironment()
  private val rootPackage = new MoePackage("main", rootEnv)
  private val corePackage = new MoePackage("CORE", new MoeEnvironment(Some(rootEnv)))

  private val objectClass = new MoeClass("Object", Some(VERSION), Some(AUTHORITY))
  private val classClass  = new MoeClass("Class", Some(VERSION), Some(AUTHORITY), Some(objectClass))

  def isBootstrapped     = is_bootstrapped
  def areWarningsEnabled = warnings
  def isDebuggingOn      = debug

  def getEnv         = systemEnv
  def getIncludeDirs = includeDirs
  def addIncludeDir(path: String) = includeDirs += NativeObjects.getStr(path)

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
      objectClass.setAssociatedType(Some(MoeClassType(Some(classClass)))) // Object is a class
      classClass.setAssociatedType(Some(MoeClassType(Some(classClass))))  // Class is a class

      // we need a special subclass of Class
      // for use with our core classes, this
      // allows us to better control their 
      // constructors
      val coreClassClass = new MoeClass("CoreClass", Some(VERSION), Some(AUTHORITY), Some(classClass))

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

      // undef is special, it is kind of a top-type instance
      val undefClass     = new MoeClass("Undef",      Some(VERSION), Some(AUTHORITY), Some(anyClass))

      val scalarClass    = new MoeClass("Scalar",     Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val arrayClass     = new MoeClass("Array",      Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val hashClass      = new MoeClass("Hash",       Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val pairClass      = new MoeClass("Pair",       Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val ioClass        = new MoeClass("IO",         Some(VERSION), Some(AUTHORITY), Some(anyClass))
      val codeClass      = new MoeClass("Code",       Some(VERSION), Some(AUTHORITY), Some(anyClass))

      val boolClass      = new MoeClass("Bool",       Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val strClass       = new MoeClass("Str",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val intClass       = new MoeClass("Int",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val numClass       = new MoeClass("Num",        Some(VERSION), Some(AUTHORITY), Some(scalarClass))
      val exceptionClass = new MoeClass("Exception",  Some(VERSION), Some(AUTHORITY), Some(scalarClass))

      // set the associated class for all classes
      // this must be classClass because these are
      // instances of Class
      anyClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      scalarClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      arrayClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      hashClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      pairClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      ioClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      codeClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      
      undefClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      boolClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      strClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      intClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      numClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))
      exceptionClass.setAssociatedType(Some(MoeClassType(Some(coreClassClass))))

      // add all these classes to the corePackage
      corePackage.addClass(objectClass)
      corePackage.addClass(classClass)
      corePackage.addClass(coreClassClass)

      corePackage.addClass(anyClass)
      corePackage.addClass(scalarClass)
      corePackage.addClass(arrayClass)
      corePackage.addClass(hashClass)
      corePackage.addClass(pairClass)
      corePackage.addClass(ioClass)
      corePackage.addClass(codeClass)

      corePackage.addClass(undefClass)
      corePackage.addClass(boolClass)
      corePackage.addClass(strClass)
      corePackage.addClass(intClass)
      corePackage.addClass(numClass)
      corePackage.addClass(exceptionClass)

      setupBuiltins

      rootEnv.setCurrentRuntime(this)

      is_bootstrapped = true

      // do some basic environment setup now that we are bootstrapped

      getEnv ++= system.getEnv.map({ case (k, v) => k -> NativeObjects.getStr(v) })
      addIncludeDir(new java.io.File(".").getCanonicalPath)
    }
  }

  def getCoreClassFor      (name: String): Option[MoeClass]      = corePackage.getClass(name)
  def getCoreSubroutineFor (name: String): Option[MoeSubroutine] = corePackage.getSubroutine(name)

  private def deconstructNamespace(full: String): (Option[Array[String]], String) = {
    val split_name = full.split("::")
    if (split_name.length == 1) 
      (None -> split_name.last)
    else   
      (Some(split_name.dropRight(1)) -> split_name.last)
  }

  def lookupSubroutine (name: String, pkg: MoePackage): Option[MoeSubroutine] = (deconstructNamespace(name) match {
    case (None,           sub_name) => pkg.getSubroutine(sub_name)
    case (Some(pkg_name), sub_name) => MoePackage.findPackageByName(pkg_name, pkg).flatMap(_.getSubroutine(sub_name))
  }).orElse(
    if (name.contains("::")) {
      lookupSubroutine(name, rootPackage).orElse(getCoreSubroutineFor(name))
    } else {
      getCoreSubroutineFor(name)
    }
  )

  def lookupClass (name: String, pkg: MoePackage): Option[MoeClass] = (deconstructNamespace(name) match {
    case (None,           class_name) => pkg.getClass(class_name)
    case (Some(pkg_name), class_name) => MoePackage.findPackageByName(pkg_name, pkg).flatMap(_.getClass(class_name))
  }).orElse(
    if (name.contains("::")) {
      lookupClass(name, rootPackage).orElse(getCoreClassFor(name))
    } else {
      getCoreClassFor(name)
    }
  )

  def findFilePathForPackageName (full: String): Option[java.io.File] = {
    val p = "/" + full.split("::").mkString("/") + ".mo"
    includeDirs.find(
      d => new java.io.File(d.unboxToString.get + p).exists()
    ).map(
      d => new java.io.File(d.unboxToString.get + p)
    )
  }

  // TODO: 
  // add line numbers and such 
  // to the message, when we have
  // actual line numbers that is
  // - SL
  def warn  (msg: MoeObject*): Unit = system.getSTDERR.println(msg.map(_.unboxToString.get).mkString)
  def print (msg: MoeObject*): Unit = system.getSTDOUT.print(msg.map(_.unboxToString.get).mkString)
  def say   (msg: MoeObject*): Unit = system.getSTDOUT.println(msg.map(_.unboxToString.get).mkString)

  def eval (line: String, env: MoeEnvironment) = interpreter.get.compile_and_evaluate(
    env, 
    CompilationUnitNode(ScopeNode(MoeParser.parseFromEntry(line)))
  )

  def getInterpreterCallStack: List[MoeStackFrame] = interpreter.get.getCallStack

  def convertInterpreterStackFrame (frame: MoeStackFrame) = this.NativeObjects.getArray(
    frame.getCurrentPackage.map((x) => this.NativeObjects.getStr(x.getFullyQualifiedName)).getOrElse(this.NativeObjects.getUndef),
    frame.getCurrentClass.map((x) => this.NativeObjects.getStr(x.getName)).getOrElse(this.NativeObjects.getUndef),
    this.NativeObjects.getStr(frame.getCode.getName),
    frame.getCurrentInvocant.getOrElse(this.NativeObjects.getUndef),
    this.NativeObjects.getArray(frame.getArgs:_*)
  )

  private def setupBuiltins = {
    import org.moe.runtime.builtins._

    ClassClass(this) 
    ObjectClass(this)
    CoreClassClass(this)

    AnyClass(this)
    ScalarClass(this)
    ArrayClass(this)
    HashClass(this)
    PairClass(this)
    IOClass(this)
    CodeClass(this)

    UndefClass(this)
    BoolClass(this)
    StrClass(this)
    IntClass(this)
    NumClass(this)
    ExceptionClass(this)

    CorePackage(this)

    RootEnvironment(this)
  }

  object NativeObjects {
    import org.moe.runtime.nativeobjects._

    private lazy val Undef = new MoeUndefObject(Some(MoeAnyType(getCoreClassFor("Undef"))))
    private lazy val True  = new MoeBoolObject(true, Some(MoeScalarType(getCoreClassFor("Bool"))))
    private lazy val False = new MoeBoolObject(false, Some(MoeScalarType(getCoreClassFor("Bool"))))

    def getUndef = Undef
    def getTrue  = True
    def getFalse = False

    def getInt  (value: Int)     = new MoeIntObject(value, Some(MoeScalarType(getCoreClassFor("Int"))))
    def getNum  (value: Double)  = new MoeNumObject(value, Some(MoeScalarType(getCoreClassFor("Num"))))
    def getStr  (value: String)  = new MoeStrObject(value, Some(MoeScalarType(getCoreClassFor("Str"))))
    def getBool (value: Boolean) = if (value) { True } else { False }

    def getPair  (value: (String, MoeObject))  = new MoePairObject((value._1, value._2), Some(MoeScalarType(getCoreClassFor("Pair"))))

    def getHash  (value: (String, MoeObject)*)     = new MoeHashObject(HashMap(value:_*), Some(MoeHashType(getCoreClassFor("Hash"))))
    def getHash  (hash: HashMap[String,MoeObject]) = new MoeHashObject(hash, Some(MoeArrayType(getCoreClassFor("Hash"))))

    def getArray (value: MoeObject*)             = new MoeArrayObject(ArrayBuffer(value:_*), Some(MoeArrayType(getCoreClassFor("Array"))))
    def getArray (array: ArrayBuffer[MoeObject]) = new MoeArrayObject(array, Some(MoeArrayType(getCoreClassFor("Array"))))

    def getIO (path: String) = new MoeIOObject(new java.io.File(path), Some(MoeScalarType(getCoreClassFor("IO"))))
    def getIO (file: java.io.File) = new MoeIOObject(file, Some(MoeScalarType(getCoreClassFor("IO"))))

    def getException (): MoeOpaque = new MoeOpaque(Some(MoeScalarType(getCoreClassFor("Exception"))))
    def getException (msg: String): MoeOpaque = {
      val e = getException()
      e.setValue("$!msg", getStr(msg))
      e
    }

    def fixupSubroutine (c: MoeSubroutine): MoeSubroutine = {
      c.setAssociatedType(Some(MoeCodeType(getCoreClassFor("Code"))))
      c
    }
  }

}
