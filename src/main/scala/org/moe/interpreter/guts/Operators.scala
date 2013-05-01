package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import InterpreterUtils._

object Operators {

  def apply (i: Interpreter, r: MoeRuntime, env: MoeEnvironment): PartialFunction[AST, MoeObject] = {
    // unary operators

    case PrefixUnaryOpNode(lhs: AST, operator: String) => {
      val receiver = i.eval(r, env, lhs)
      i.callMethod(receiver, "prefix:<" + operator + ">", List())
    }

    case PostfixUnaryOpNode(lhs: AST, operator: String) => {
      val receiver = i.eval(r, env, lhs)
      i.callMethod(receiver, "postfix:<" + operator + ">", List())
    }

    // binary operators

    case BinaryOpNode(lhs: AST, operator: String, rhs: AST) => {
      val receiver = i.eval(r, env, lhs)
      val arg      = i.eval(r, env, rhs)
      i.callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // short circuit binary operators
    // rhs is not evaluated, unless it is necessary

    case ShortCircuitBinaryOpNode(lhs: AST, operator: String, rhs: AST) => {
      val receiver = i.eval(r, env, lhs)
      val arg      = new MoeLazyEval(i, r, env, rhs)
      i.callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // ternary operator

    case TernaryOpNode(cond: AST, trueExpr: AST, falseExpr: AST) => {
      val receiver = i.eval(r, env, cond)
      val argTrue  = new MoeLazyEval(i, r, env, trueExpr)
      val argFalse = new MoeLazyEval(i, r, env, falseExpr)
      i.callMethod(receiver, "infix:<?:>", List(argTrue, argFalse))
    }
  }
}