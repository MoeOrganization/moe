package org.moe.runtime

/**
 * TODO:
 * Need to hook up these classes with
 * their runtime counterparts, when
 * we actually have them that is.
 * - SL
 */
object MoeErrors {
  class MoeException          (msg: String) extends Exception(msg)
  class MoeMoney              (msg: String) extends MoeException(msg)

  // MoeProblems is derived from MoeMoney - RIP B.I.G
  class MoeProblems           (msg: String) extends MoeMoney(msg)

  // Moe internal errors
  class MoeStartupError       (msg: String) extends MoeProblems(msg)
  class MoeTypeError          (msg: String) extends MoeProblems(msg)

  // Runtime errors
  class NotAllowed            (msg: String) extends MoeProblems(msg)
  class MethodNotAllowed      (msg: String) extends NotAllowed(msg)

  class UnknownNode           (msg: String) extends MoeProblems(msg)

  class ValueNotFound         (msg: String) extends MoeProblems(msg)
  class PackageNotFound       (msg: String) extends ValueNotFound(msg)
  class InstanceValueNotFound (msg: String) extends ValueNotFound(msg)
  class ClassNotFound         (msg: String) extends ValueNotFound(msg)
  class SuperclassNotFound    (msg: String) extends ValueNotFound(msg)
  class InvocantNotFound      (msg: String) extends ValueNotFound(msg) 
  class MethodNotFound        (msg: String) extends ValueNotFound(msg)
  class AttributeNotFound     (msg: String) extends ValueNotFound(msg)
  class SubroutineNotFound    (msg: String) extends ValueNotFound(msg)
  class VariableNotFound      (msg: String) extends ValueNotFound(msg)

  class UndefinedValue        (msg: String) extends MoeProblems(msg)
  class UndefinedMethod       (msg: String) extends UndefinedValue(msg)
  class UndefinedSubroutine   (msg: String) extends UndefinedValue(msg)

  class MissingValue          (msg: String) extends MoeProblems(msg)
  class MissingClass          (msg: String) extends MissingValue(msg)

  class UnexpectedType        (msg: String) extends MoeTypeError(msg)
  class BadTypeCoercion       (msg: String) extends MoeTypeError(msg)
}
