package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import InterpreterUtils._

object Packages {

  def declaration (i: Interpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, PackageDeclarationNode(name, body, version, authority)) => {
      val newEnv = new MoeEnvironment(Some(env))
      val parent = env.getCurrentPackage.getOrElse(throw new MoeErrors.PackageNotFound("__PACKAGE__"))
      val pkgs   = MoePackage.createPackageTreeFromName(name, version, authority, newEnv, parent)
      // attach the root
      parent.addSubPackage(pkgs._1) 
      // make the leaf the current package 
      newEnv.setCurrentPackage(pkgs._2) 

      val result = i.eval(r, newEnv, body)
      newEnv.setCurrentPackage(parent)
      // return the root
      pkgs._1
    }
  }
}