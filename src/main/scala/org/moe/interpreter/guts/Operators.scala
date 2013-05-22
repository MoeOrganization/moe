package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Operators extends Utils {

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    // unary operators

    case (env, PrefixUnaryOpNode(lhs: AST, operator: String)) => {
      val receiver = i.evaluate(env, lhs)
      callMethod(receiver, "prefix:<" + operator + ">", List())
    }

    case (env, PostfixUnaryOpNode(lhs: AST, operator: String)) => {
      val receiver = i.evaluate(env, lhs)
      callMethod(receiver, "postfix:<" + operator + ">", List())
    }

    // assignment

    case (env, BinaryOpNode(lhs: AST, "=", rhs: AST)) => {
      lhs match {
        case VariableNameNode(var_)             => i.evaluate(env, VariableAssignmentNode(var_, rhs))
        case AttributeNameNode(attr)            => i.evaluate(env, AttributeAssignmentNode(attr, rhs))
        case ArrayElementNameNode(array, index) => i.evaluate(env, ArrayElementLvalueNode(array, index, rhs))
        case HashElementNameNode(hash, key)     => i.evaluate(env, HashElementLvalueNode(hash, key, rhs))
      }
    }

    // other binary operators

    case (env, BinaryOpNode(lhs: AST, operator: String, rhs: AST)) => {
      val receiver = i.evaluate(env, lhs)
      val arg      = i.evaluate(env, rhs)
      callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // short circuit binary operators
    // rhs is not evaluateuated, unless it is necessary

    case (env, ShortCircuitBinaryOpNode(lhs: AST, operator: String, rhs: AST)) => {
      val receiver = i.evaluate(env, lhs)
      val arg      = new MoeLazyEval(i, env, rhs)
      callMethod(receiver, "infix:<" + operator + ">", List(arg))
    }

    // ternary operator

    case (env, TernaryOpNode(cond: AST, trueExpr: AST, falseExpr: AST)) => {
      val receiver = i.evaluate(env, cond)
      val argTrue  = new MoeLazyEval(i, env, trueExpr)
      val argFalse = new MoeLazyEval(i, env, falseExpr)
      callMethod(receiver, "infix:<?:>", List(argTrue, argFalse))
    }
  }
}
