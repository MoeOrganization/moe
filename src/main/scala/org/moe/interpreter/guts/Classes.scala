package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

import InterpreterUtils._

object Classes {

  def declaration (i: Interpreter, r: MoeRuntime, env: MoeEnvironment): PartialFunction[AST, MoeObject] = {
    case ClassDeclarationNode(name, superclass, body, version, authority) => {
      val pkg = env.getCurrentPackage.getOrElse(
        throw new MoeErrors.PackageNotFound("__PACKAGE__")
      )

      val superclass_class: Option[MoeClass] = superclass.map(
        r.lookupClass(_, pkg).getOrElse(
          throw new MoeErrors.ClassNotFound(superclass.getOrElse(""))
        )
      ).orElse(r.getCoreClassFor("Any"))

      val klass = new MoeClass(
        name, 
        version, 
        authority, 
        superclass_class
      )

      klass.setAssociatedType(Some(MoeClassType(r.getCoreClassFor("Class"))))

      pkg.addClass(klass)

      val klass_env = new MoeEnvironment(Some(env))
      klass_env.setCurrentClass(klass)
      i.eval(r, klass_env, body)

      klass
    }

    case SubMethodDeclarationNode(name, signature, body) => {
      val klass = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
      val sig = i.eval(r, env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)
      val method = new MoeMethod(
        name            = name,
        signature       = sig,
        declaration_env = env,
        body            = (e) => i.eval(r, e, body)
      )
      klass.addSubMethod(method)
      method
    }

    case MethodDeclarationNode(name, signature, body) => {
      val klass = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
      val sig = i.eval(r, env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)
      val method = new MoeMethod(
        name            = name,
        signature       = sig,
        declaration_env = env,
        body            = (e) => i.eval(r, e, body)
      )
      klass.addMethod(method)
      method
    }

    case AttributeDeclarationNode(name, expression) => {
      val klass = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
      val attr_default = () => i.eval(r, env, expression)
      val attr = new MoeAttribute(name, Some(attr_default))
      klass.addAttribute(attr)
      attr
    }
  }

  def apply (i: Interpreter, r: MoeRuntime, env: MoeEnvironment): PartialFunction[AST, MoeObject] = {
    case ClassAccessNode(name) => r.lookupClass(
      name, 
      env.getCurrentPackage.getOrElse(throw new MoeErrors.PackageNotFound("__PACKAGE__"))
    ).getOrElse( 
      r.lookupClass(name, r.getRootPackage).getOrElse(throw new MoeErrors.ClassNotFound(name))
    )

    case AttributeAccessNode(name) => {
      val klass    = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
      val attr     = klass.getAttribute(name).getOrElse(throw new MoeErrors.AttributeNotFound(name))
      val invocant = env.getCurrentInvocant
      invocant match {
        case Some(invocant: MoeOpaque) => invocant.getValue(name).getOrElse(
          throw new MoeErrors.InstanceValueNotFound(name)
        )
        case _ => throw new MoeErrors.UnexpectedType(invocant.getOrElse("(undef)").toString)
      }
    }

    case AttributeAssignmentNode(name, expression) => {
      val klass = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
      val attr  = klass.getAttribute(name).getOrElse(throw new MoeErrors.AttributeNotFound(name))
      val expr  = i.eval(r, env, expression)

      if (!MoeType.checkType(name, expr)) throw new MoeErrors.IncompatibleType(
          "the container (" + name + ") is not compatible with " + expr.getAssociatedType.get.getName
        )

      env.getCurrentInvocant match {
        case Some(invocant: MoeOpaque) => invocant.setValue(name, expr)
        case Some(invocant)            => throw new MoeErrors.UnexpectedType(invocant.toString)
        case None                      => throw new MoeErrors.MoeException("Attribute default already declared")
      }
      expr
    }

    case MultiAttributeAssignmentNode(names, expressions) => {
      val klass = env.getCurrentClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))

      val evaled_expressions = expressions.map(i.eval(r, env, _)) 
      env.getCurrentInvocant match {
        case Some(invocant: MoeOpaque) => {
          i.zipVars(
            r,
            names, 
            evaled_expressions, 
            { 
              case (name, value) => {
                if (!MoeType.checkType(name, value)) throw new MoeErrors.IncompatibleType(
                    "the container (" + name + ") is not compatible with " + value.getAssociatedType.get.getName
                  )
                klass.getAttribute(name).getOrElse(throw new MoeErrors.AttributeNotFound(name))
                invocant.setValue(name, value)
              }
            }
          )
        }
        case Some(invocant) => throw new MoeErrors.UnexpectedType(invocant.toString)
        case None           => throw new MoeErrors.MoeException("Attribute default already declared")
      }

      evaled_expressions.last
    }

    case MethodCallNode(invocant, method_name, args) => {
      val invocant_object = i.eval(r, env, invocant)
      invocant_object match {
        case obj: MoeObject =>
          val klass = obj.getAssociatedClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
          val meth = klass.getMethod(method_name).getOrElse(
            klass.getSubMethod(method_name).getOrElse(
              throw new MoeErrors.MethodNotFound(method_name)
            )
          )
          obj.callMethod(meth, args.map(i.eval(r, env, _)))
        case _ => throw new MoeErrors.MoeException("Object expected")
      }
    }

  }
}