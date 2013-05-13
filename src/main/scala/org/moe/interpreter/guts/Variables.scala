package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Variables extends Utils {

  def declaration (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, VariableDeclarationNode(name, expression)) => {
      val value = i.evaluate(env, expression)
      if (!MoeType.checkType(name, value)) throw new MoeErrors.IncompatibleType(
          "the container (" + name + ") is not compatible with " + value.getAssociatedType.get.getName
        )
      env.create(name, value).get
    }
  }

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, VariableAccessNode(name)) => env.get(name).getOrElse(
        if (name.startsWith("&")) {
          val function_name = name.drop(1)
          r.lookupSubroutine(
            function_name, 
            getCurrentPackage(env)
          ).getOrElse( 
            throw new MoeErrors.SubroutineNotFound(function_name)
          )
        } else {
          throw new MoeErrors.VariableNotFound(name)
        }
      )

    case (env, MultiVariableAssignmentNode(names, expressions)) => {
      zipVars(
        r,
        names, 
        expressions.map(i.evaluate(env, _)), 
        { 
          case (name, value) => {
            if (!MoeType.checkType(name, value)) throw new MoeErrors.IncompatibleType(
                "the container (" + name + ") is not compatible with " + value.getAssociatedType.get.getName
              )
            env.set(name, value).getOrElse(throw new MoeErrors.VariableNotFound(name))
          }
        }
      )
      env.get(names.last).getOrElse(throw new MoeErrors.VariableNotFound(names.last))
    }

    case (env, VariableAssignmentNode(name, expression)) => {
      val value = i.evaluate(env, expression)
      if (!MoeType.checkType(name, value)) throw new MoeErrors.IncompatibleType(
          "the container (" + name + ") is not compatible with " + value.getAssociatedType.get.getName
        )
      env.set(name, value).getOrElse(
        throw new MoeErrors.VariableNotFound(name)
      )
    }

  }
}