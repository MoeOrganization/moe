package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Subroutines extends Utils {

  def declaration (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, SubroutineDeclarationNode(name, signature, body, traits)) => {
      val sig = i.compile(env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)

      val pkg = getCurrentPackage(env)

      val decl_env = new MoeEnvironment(Some(env))
      decl_env.setCurrentPackage(pkg)

      val sub = new MoeSubroutine(
        name            = name,
        signature       = sig,
        declaration_env = decl_env,
        body            = (e) => i.evaluate(e, body),
        traits          = traits.getOrElse(List()).map(MoeSubroutineTraits.withName(_))
      )

      pkg.addSubroutine( sub )
      sub
    }
  }

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, SubroutineCallNode(function_name, args)) => {
      val sub = r.lookupSubroutine(function_name, getCurrentPackage(env)).getOrElse( 
        throw new MoeErrors.SubroutineNotFound(function_name)
      )

      val evaluated_args = args.map(i.evaluate(env, _))

      i.pushCallStack(new MoeStackFrame(sub, evaluated_args, env)) 
      val result = sub.execute(new MoeArguments(evaluated_args))
      i.popCallStack
      result
    }
  }
}