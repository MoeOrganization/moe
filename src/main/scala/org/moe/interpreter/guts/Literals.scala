package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Literals extends Utils {

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, IntLiteralNode(value))     => r.NativeObjects.getInt(value)
    case (env, FloatLiteralNode(value))   => r.NativeObjects.getNum(value)
    case (env, StringLiteralNode(value))  => r.NativeObjects.getStr(value)
    case (env, BooleanLiteralNode(value)) => r.NativeObjects.getBool(value)
    case (env, UndefLiteralNode())        => r.NativeObjects.getUndef


    case (env, SelfLiteralNode())  => env.getCurrentInvocant.getOrElse(
        throw new MoeErrors.InvocantNotFound("__SELF__")
      )
    case (env, ClassLiteralNode()) => env.getCurrentClass.getOrElse(
        throw new MoeErrors.ClassNotFound("__CLASS__")
      )

    case (env, SuperLiteralNode()) => {
      val klass = env.getCurrentClass
      klass.getOrElse(
        throw new MoeErrors.ClassNotFound("__CLASS__")
      ).getSuperclass.getOrElse(
        throw new MoeErrors.SuperclassNotFound(klass.get.getName)
      )
    }

    case (env, ArrayLiteralNode(values)) => r.NativeObjects.getArray(values.map(i.evaluate(env, _)):_*)

    case (env, PairLiteralNode(key, value)) => r.NativeObjects.getPair(
      i.evaluate(env, key).unboxToString.get -> i.evaluate(env, value)
    )

    case (env, HashLiteralNode(values)) => r.NativeObjects.getHash(values.map(i.evaluate(env, _).unboxToTuple.get):_*)

    case (env, CodeLiteralNode(signature, body)) => {
      val sig = i.compile(env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)
      val code = new MoeCode(
        signature       = sig,
        declaration_env = env,
        body            = (e) => i.evaluate(e, body)
      )
      // FIXME:
      // This should probably be done
      // through some kind of factory 
      // constructor, and we also need
      // to do it with the subs and 
      // methods eventually (once we 
      // have a MOP to expose)
      // But for now this will do.
      // - SL
      code.setAssociatedType(Some(MoeCodeType(r.getCoreClassFor("Code"))))
      code
    }

    case (env, RangeLiteralNode(start, end)) => {
      val s = i.evaluate(env, start)
      val e = i.evaluate(env, end)

      var result: List[MoeObject] = List();

      (s, e) match {
        case (s: MoeIntObject, e: MoeIntObject) => {
          val range_start  = s.unboxToInt.get
          val range_end    = e.unboxToInt.get
          val range: Range = new Range(range_start, range_end + 1, 1)
          result = range.toList.map(r.NativeObjects.getInt(_))
        }
        case (s: MoeStrObject, e: MoeStrObject) => {
          val range_start = s.unboxToString.get
          val range_end   = e.unboxToString.get

          if (range_start.length <= range_end.length) {
            var elems: List[String] = List()
            var str = range_start
            while (str <= range_end || str.length < range_end.length) {
              elems = elems :+ str
              str = MoeUtil.magicalStringIncrement(str)
            }
            result = elems.map(r.NativeObjects.getStr(_))
          }
        }
        case _ => throw new MoeErrors.UnexpectedType("Pair of MoeIntObject or MoeStrObject expected")
      }

      r.NativeObjects.getArray(result:_*)
    }

  }

}