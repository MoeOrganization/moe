package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import scala.util.{Try, Success, Failure}

object ElementAccess {

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {

    case (env, ArrayElementAccessNode(arrayName: String, indices: List[AST])) => {
      var array_value = env.get(arrayName) match {
         case Some(a: MoeArrayObject) => a
         case _ => throw new MoeErrors.UnexpectedType("MoeArrayObject expected")
      }

      indices.foldLeft[MoeObject](array_value) {
        (array, item) =>
          val index = i.evaluate(env, item)
          index match {
            case int: MoeIntObject => i.callMethod(array, "postcircumfix:<[]>", List(int), "Array")
            case obj: MoeObject => {
              obj.unboxToArrayBuffer match {
                case Success(a) => i.callMethod(array, "slice", a.toList, "Array")
                case _          => i.callMethod(array, "slice", List(obj),  "Array")
              }
            }
          }
      }
    }

    case (env, ArrayElementLvalueNode(arrayName: String, indices: List[AST], expr: AST)) => {
      val array_value = env.get(arrayName) match {
        case Some(a: MoeArrayObject) => a
        case _ => throw new MoeErrors.UnexpectedType("MoeArrayObject expected")
      }

      // find the deepest array and position that will be assigned
      var last_index = i.evaluate(env, indices.last)
      val last_array = indices.dropRight(1).foldLeft[MoeObject](array_value) {
        (array, item) =>
          val index = i.evaluate(env, item)
          i.callMethod(array, "postcircumfix:<[]>", List(index), "Array")
      }

      // perform the assignment
      val expr_value = i.evaluate(env, expr)
      i.callMethod(last_array, "postcircumfix:<[]>", List(last_index, expr_value), "Array")
    }

    case (env, HashElementAccessNode(hashName: String, keys: List[AST])) => {
      val hash_map = env.get(hashName) match {
        case Some(h: MoeHashObject) => h
        case _ => throw new MoeErrors.UnexpectedType("MoeHashObject expected")
      }

      keys.foldLeft[MoeObject](hash_map) {
        (h, k) =>
          val key = i.evaluate(env, k)
          i.callMethod(h, "postcircumfix:<{}>", List(key), "Hash")
      }
    }

    case (env, HashElementLvalueNode(hashName: String, keys: List[AST], value: AST)) => {
      val hash_map = env.get(hashName) match {
        case Some(h: MoeHashObject) => h
        case _ => throw new MoeErrors.UnexpectedType("MoeHashObject expected")
      }

      // find the deepest hash and key that will be assigned
      val last_key = i.evaluate(env, keys.last)
      val last_hash = keys.dropRight(1).foldLeft[MoeObject](hash_map) {
        (h, k) =>
          val key = i.evaluate(env, k)
          i.callMethod(h, "postcircumfix:<{}>", List(key), "Hash")
      }

      // perform the assignment
      val value_result = i.evaluate(env, value)
      i.callMethod(last_hash, "postcircumfix:<{}>", List(last_key, value_result), "Hash")
    }
  }

}