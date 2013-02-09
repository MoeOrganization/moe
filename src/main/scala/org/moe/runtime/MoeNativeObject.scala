package org.moe.runtime

abstract class MoeNativeObject[A] (
  private val value: A,
  private var associatedClass: Option[MoeClass] = None)
  extends MoeObject(associatedClass) {

  def getNativeValue: A = value
}

// NOTE:
// It will likely be useful to add more methods
// to all of these classes, if for no other reason
// then to keep the boxing/unboxing logic in one
// single place. At which point, it will also
// become useful to break these out into separate
// source files as well.
// - SL

// Simple objects

class MoeIntObject(v: Int, klass : Option[MoeClass] = None) extends MoeNativeObject[Int](v, klass) {
  override def isFalse: Boolean = getNativeValue == 0
  override def toString = getNativeValue.toString

  klass.map({klass =>
    klass.addMethod(
      new MoeMethod(
        "+",
        { (lhs, args) =>
          val rhs = args(0)
          lhs match {
            case i: MoeIntObject =>
              rhs match {
                case rhs_i: MoeIntObject => new MoeIntObject(i.getNativeValue + rhs_i.getNativeValue)
                case rhs_n: MoeFloatObject => new MoeIntObject(i.getNativeValue + rhs_n.getNativeValue.toInt)
                case _ => throw new MoeErrors.UnexpectedType(rhs.toString)
              }
          }
        }
      )
    )
  })
}

class MoeFloatObject(v: Double, klass : Option[MoeClass] = None) extends MoeNativeObject[Double](v, klass) {
  override def isFalse: Boolean = getNativeValue == 0
  override def toString = getNativeValue.toString
}

class MoeStringObject(v: String, klass : Option[MoeClass] = None) extends MoeNativeObject[String](v, klass) {
  override def isFalse: Boolean = getNativeValue match {
    case "" | "0" => true
    case _        => false
  }
  override def toString = '"' + getNativeValue + '"'
}

class MoeBooleanObject(v: Boolean, klass : Option[MoeClass] = None) extends MoeNativeObject[Boolean](v, klass) {
  override def isFalse: Boolean = getNativeValue == false
  override def toString: String = if (getNativeValue) "true" else "false"
}

// this is the one outlyer, it doesn't need to be
// a NativeObject because it doesn't need to actually
// box any values, just the lack of a value
class MoeNullObject(klass : Option[MoeClass] = None) extends MoeObject(klass) {
  def getNativeValue: AnyRef  = null
  override def isFalse: Boolean = true
  override def isUndef: Boolean = true
  override def toString: String = "null"
}

// Complex objects

// NOTE:
// List[A] is a poor internal representation
// for a MoeArray, this will need to be thought
// through much more. It looks like ListBuffer
// could be suitable, but we might need to just
// write our own in the long run.
// - SL
class MoeArrayObject(
    v: List[MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[List[MoeObject]](v, klass) {
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '[' + getNativeValue.map(_.toString).mkString(", ") + ']'
}

class MoeHashObject(
    v: Map[String, MoeObject],
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[Map[String, MoeObject]](v, klass) {
  override def isFalse: Boolean = getNativeValue.size == 0
  override def toString: String =
    '{' + getNativeValue.map({
      case (k, v) => k + " => " + v.toString
    }).mkString(", ") + '}'
}

class MoePairObject(
    v: (String, MoeObject), 
    klass : Option[MoeClass] = None
  ) extends MoeNativeObject[(String, MoeObject)](v, klass) {
  override def toString: String = getNativeValue match {
    case (k, v) =>
      k + " => " + v.toString
  }
}
