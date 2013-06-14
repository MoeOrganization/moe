package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._
import org.moe.parser._

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

    // regexes

    case (env, BinaryOpNode(lhs: AST, "=~", rhs: AST)) => {
      rhs match {
        case MatchExpressionNode(pattern, flags) => i.evaluate(env, RegexMatchNode(lhs, pattern, flags))
        // TODO: interpolation of the regex variable value
        case VariableAccessNode (pattern)        => i.evaluate(env, RegexMatchNode(lhs, rhs, StringLiteralNode("")))
        case SubstExpressionNode(pattern, replacement, flags) => i.evaluate(env, RegexSubstNode(lhs, pattern, replacement, flags))
        case TransExpressionNode(search,  replacement, flags) => i.evaluate(env, TransOpNode(lhs, search, replacement, flags))
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

    // regex operations

    case (env, RegexMatchNode(target: AST, pattern: AST, flags: AST)) => {
      val receiver = i.evaluate(env, target)
      val argPattern = i.evaluate(env, pattern)
      val argFlags = i.evaluate(env, flags);
      callMethod(receiver, "match", List(argPattern, argFlags))
    }

    case (env, RegexSubstNode(target: AST, pattern: AST, replacement: AST, flags: AST)) => {
      val receiver = i.evaluate(env, target)
      val argPattern = i.evaluate(env, pattern)
      val argReplacement = i.evaluate(env, replacement)
      val argFlags = i.evaluate(env, flags);
      callMethod(receiver, "subst", List(argPattern, argReplacement, argFlags))
    }

    case (env, TransOpNode(target: AST, search: AST, replacement: AST, flags: AST)) => {
      val receiver = i.evaluate(env, target)
      val argSearch = i.evaluate(env, search)
      val argReplacement = i.evaluate(env, replacement)
      val argFlags = i.evaluate(env, flags);
      callMethod(receiver, "trans", List(argSearch, argReplacement, argFlags))
    }

  }
}
