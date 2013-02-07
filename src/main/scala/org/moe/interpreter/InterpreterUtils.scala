package org.moe.interpreter

import org.moe.runtime._

object InterpreterUtils {
  def objToNumeric(obj: MoeObject): Double = obj match {
    case i: MoeIntObject => i.getNativeValue.toDouble
    case n: MoeFloatObject => n.getNativeValue
    case _ => throw new MoeErrors.MoeException("Could not coerce object into numeric")
  }

  def objToInteger(obj: MoeObject): Int = obj match {
    case i: MoeIntObject => i.getNativeValue
    case n: MoeFloatObject => n.getNativeValue.toInt
    case _ => throw new MoeErrors.MoeException("Could not coerce object into integer")
  }

  def objToString(obj: MoeObject): String = obj match {
    case i: MoeIntObject => i.getNativeValue.toString
    case n: MoeFloatObject => n.getNativeValue.toString
    case s: MoeStringObject => s.getNativeValue
    case _ => throw new MoeErrors.MoeException("Could not coerce object into string")
  }

  def inNewEnv[T](env: MoeEnvironment)(body: MoeEnvironment => T): T = {
    val newEnv = new MoeEnvironment(Some(env))

    body(env)
  }
}

