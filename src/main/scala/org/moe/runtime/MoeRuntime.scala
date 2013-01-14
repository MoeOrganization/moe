package org.moe.runtime

object Runtime {

  private val RootEnv = new MoeEnvironment()

  def getRootEnv = RootEnv

  // TODO:
  // all the objects that come out
  // from this factory actually need
  // to have a MoeClass associated with
  // them, since I have not defined them
  // yet I am punting for now.
  // - SL
  object NativeObjects {

    private val Undef = new MoeNullObject()
    private val True  = new MoeBooleanObject(true)
    private val False = new MoeBooleanObject(false)

    def getUndef = Undef
    def getTrue = True
    def getFalse = False

    def getInt(value: Int) = new MoeIntObject(value)
    def getFloat(value: Double) = new MoeFloatObject(value)
    def getString(value: String) = new MoeStringObject(value)

    def getPair(value: (String, MoeObject)) = new MoePairObject(value)
    def getHash(value: Map[String, MoeObject]) = new MoeHashObject(value)
    def getArray(value: List[MoeObject]) = new MoeArrayObject(value)
  }

  // TODO:
  // Need to hook up these classes with
  // their runtime counterparts, when
  // we actually have them that is.
  // - SL
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