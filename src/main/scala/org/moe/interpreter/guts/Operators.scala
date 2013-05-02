package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Operators {

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    // unary operators

    case (env, PrefixUnaryOpNode(lhs: AST, operator: String)) => {
      val receiver = i.evaluate(env, lhs)
      i.callMethod(receiver, "prefix:<" + operator + ">", List())
    }

    case (env, PostfixUnaryOpNode(lhs: AST, operator: String)) => {
      val receiver = i.evaluate(env, lhs)
      i.callMethod(receiver, "postfix:<" + operator + ">", List())
    }

    // binary operators

    case (env, BinaryOpNode(lhs: AST, operator: String, rhs: AST)) => {
      val receiver = i.evaluate(env, lhs)
      val arg      = i.evaluate(env, rhs)
      i.callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // short circuit binary operators
    // rhs is not evaluateuated, unless it is necessary

    case (env, ShortCircuitBinaryOpNode(lhs: AST, operator: String, rhs: AST)) => {
      val receiver = i.evaluate(env, lhs)
      val arg      = new MoeLazyEval(i, r, env, rhs)
      i.callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // ternary operator

    case (env, TernaryOpNode(cond: AST, trueExpr: AST, falseExpr: AST)) => {
      val receiver = i.evaluate(env, cond)
      val argTrue  = new MoeLazyEval(i, r, env, trueExpr)
      val argFalse = new MoeLazyEval(i, r, env, falseExpr)
      i.callMethod(receiver, "infix:<?:>", List(argTrue, argFalse))
    }
  }
}