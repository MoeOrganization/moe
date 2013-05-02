package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._
import org.moe.parser._

import scala.io.Source

object Statements {

  private val stub = new MoeObject()

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, UseStatement(name)) => {
      val path = r.findFilePathForPackageName(name).getOrElse(
        throw new MoeErrors.MoeProblems(
          "Could not find module " + name + " in @INC [" + r.getIncludeDirs.mkString("; ") + "]"
        )       
      )

      env.getAs[MoeHashObject]("%INC").get.bind_key(
        r, 
        r.NativeObjects.getStr(name), 
        r.NativeObjects.getStr(path.toString)
      )

      val result = i.compile_and_evaluate(
        env, 
        MoeParser.parseFromEntry(Source.fromFile(path).mkString)
      )

      result match {
        case (p: MoePackage) => env.getCurrentPackage.get.importSubroutines(
          MoePackage.findPackageByName(name, r.getRootPackage).getOrElse(
            throw new MoeErrors.PackageNotFound(name)
          ).getExportedSubroutines
        )
        case _ => ()
      }

      result
    }

    case (env, IfNode(if_node)) => {
      if (i.evaluate(env, if_node.condition).isTrue) {
        i.evaluate(env, if_node.body)
      } else if (if_node.else_node.isDefined) {
        i.evaluate(env, IfNode(if_node.else_node.get))
      } else {
        r.NativeObjects.getUndef
      }
    }
        
    case (env, UnlessNode(unless_condition, unless_body)) => {
      i.evaluate(env,
        UnlessElseNode(
          unless_condition,
          unless_body,
          UndefLiteralNode()
        )
      )
    }
    case (env, UnlessElseNode(unless_condition, unless_body, else_body)) => {
      var if_node = new IfStruct(
        PrefixUnaryOpNode(unless_condition, "!"), 
        unless_body, 
        Some(
          new IfStruct(
            BooleanLiteralNode(true), 
            else_body
          )
        )
      )
      i.evaluate(env, IfNode(if_node))
    }

    case (env, TryNode(body, catch_nodes, finally_nodes)) => stub
    case (env, CatchNode(type_name, local_name, body)) => stub
    case (env, FinallyNode(body)) => stub

    case (env, WhileNode(condition, body)) => {
      val newEnv = new MoeEnvironment(Some(env))
      while (i.evaluate(newEnv, condition).isTrue) {
        i.evaluate(newEnv, body)
      }
      r.NativeObjects.getUndef // XXX
    }

    case (env, DoWhileNode(condition, body)) => {
      val newEnv = new MoeEnvironment(Some(env))
      do {
        i.evaluate(newEnv, body)
      } while (i.evaluate(newEnv, condition).isTrue)
      r.NativeObjects.getUndef // XXX
    }

    case (env, ForeachNode(topic, list, body)) => {
      i.evaluate(env, list) match {
        case objects: MoeArrayObject => {
          val applyScopeInjection = {
            (
              newEnv: MoeEnvironment, 
              name: String, 
              obj: MoeObject, 
              f: (MoeEnvironment, String, MoeObject) => Any
            ) =>
            f(env, name, obj)
            i.evaluate(newEnv, body)
          }

          val newEnv = new MoeEnvironment(Some(env))
          for (o <- objects.getNativeValue) // XXX - fix this usage of getNativeValue
            topic match {
              // XXX ran into issues trying to i.evaluate(env, ScopeNode(...))
              // since o is already i.evaluateuated at this point
              case VariableDeclarationNode(name, expr) =>
                applyScopeInjection(newEnv, name, o, (_.create(_, _)))
              // Don't do anything special here, env access will just walk back
              case VariableAccessNode(name) =>
                applyScopeInjection(newEnv, name, o, (_.set(_, _)))
            }
          r.NativeObjects.getUndef // XXX
        }
      }
    }

    case (env, ForNode(init, condition, update, body)) => {
      val newEnv = new MoeEnvironment(Some(env))
      i.compile(newEnv, init)
      while (i.evaluate(newEnv, condition).isTrue) {
        i.evaluate(newEnv, body)
        i.evaluate(newEnv, update)
      }
      r.NativeObjects.getUndef
    }
  }
}