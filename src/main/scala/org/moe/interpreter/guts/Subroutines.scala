package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import InterpreterUtils._

object Subroutines {

  def declaration (i: Interpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, SubroutineDeclarationNode(name, signature, body, traits)) => {
      val sig = i.compile(env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)

      val pkg = env.getCurrentPackage.getOrElse(throw new MoeErrors.PackageNotFound("__PACKAGE__"))

      val decl_env = new MoeEnvironment(Some(env))
      decl_env.setCurrentPackage(pkg)

      val sub = new MoeSubroutine(
        name            = name,
        signature       = sig,
        declaration_env = decl_env,
        body            = (e) => i.evaluate(e, body),
        traits          = traits.getOrElse(List())
      )

      pkg.addSubroutine( sub )
      sub
    }
  }

  def apply (i: Interpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, SubroutineCallNode(function_name, args)) => {
      val sub = r.lookupSubroutine(
        function_name, 
        env.getCurrentPackage.getOrElse(throw new MoeErrors.PackageNotFound("__PACKAGE__"))
      ).getOrElse( 
        throw new MoeErrors.SubroutineNotFound(function_name)
      )

      sub.execute(new MoeArguments(args.map(i.evaluate(env, _))))
    }
  }
}