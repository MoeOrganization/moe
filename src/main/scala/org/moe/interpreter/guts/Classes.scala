package org.moe.interpreter.guts

import org.moe.interpreter._
import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

object Classes extends Utils {

  def declaration (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, ClassDeclarationNode(name, superclass, body, version, authority)) => {
      val pkg = getCurrentPackage(env)

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
      i.compile(klass_env, body)

      klass
    }

    case (env, SubMethodDeclarationNode(name, signature, body)) => {
      val klass = getCurrentClass(env)
      val sig   = i.compile(env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)
      val method = new MoeMethod(
        name            = name,
        signature       = sig,
        declaration_env = env,
        body            = (e) => i.evaluate(e, body)
      )
      klass.addSubMethod(method)
      method
    }

    case (env, MethodDeclarationNode(name, signature, body)) => {
      val klass = getCurrentClass(env)
      val sig   = i.compile(env, signature).asInstanceOf[MoeSignature]
      throwForUndeclaredVars(env, sig, body)
      val method = new MoeMethod(
        name            = name,
        signature       = sig,
        declaration_env = env,
        body            = (e) => i.evaluate(e, body)
      )
      klass.addMethod(method)
      method
    }

    case (env, AttributeDeclarationNode(name, expression)) => {
      val klass = getCurrentClass(env)
      val attr_default = () => i.evaluate(env, expression)
      val attr = new MoeAttribute(name, Some(attr_default))
      klass.addAttribute(attr)
      attr
    }
  }

  def apply (i: MoeInterpreter, r: MoeRuntime): PartialFunction[(MoeEnvironment, AST), MoeObject] = {
    case (env, ClassAccessNode(name)) => r.lookupClass(
      name, 
      getCurrentPackage(env)
    ).getOrElse( 
      throw new MoeErrors.ClassNotFound(name)
    )

    case (env, SuperCallNode()) => {
      val superclass = getCurrentClass(env).getSuperclass.getOrElse(
        throw new MoeErrors.SuperclassNotFound("__SUPER__")
      )
      val stack_frame = i.peakCallStack
      val meth_name   = stack_frame.getCode.getName 
      val args        = stack_frame.getArgs
      val invocant    = stack_frame.getCurrentInvocant.getOrElse(
        throw new MoeErrors.InvocantNotFound("super()")
      )
      val meth        = superclass.getMethod(meth_name).getOrElse(
        throw new MoeErrors.MethodNotFound(superclass.getName + "::" + meth_name)
      )

      i.pushCallStack(new MoeStackFrame(meth, args, env, Some(invocant))) 
      val result = invocant.callMethod(meth, args)
      i.popCallStack
      result
    }

    case (env, AttributeAccessNode(name)) => {
      val klass    = getCurrentClass(env)
      val attr     = klass.getAttribute(name).getOrElse(throw new MoeErrors.AttributeNotFound(name))
      val invocant = env.getCurrentInvocant
      invocant match {
        case Some(invocant: MoeOpaque) => invocant.getValue(name).getOrElse(
          throw new MoeErrors.InstanceValueNotFound(name)
        )
        case _ => throw new MoeErrors.UnexpectedType(invocant.getOrElse("(undef)").toString)
      }
    }

    case (env, AttributeAssignmentNode(name, expression)) => {
      val klass = getCurrentClass(env)
      val attr  = klass.getAttribute(name).getOrElse(throw new MoeErrors.AttributeNotFound(name))
      val expr  = i.evaluate(env, expression)

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

    case (env, MultiAttributeAssignmentNode(names, expressions)) => {
      val klass = getCurrentClass(env)

      val evaled_expressions = expressions.map(i.evaluate(env, _)) 
      env.getCurrentInvocant match {
        case Some(invocant: MoeOpaque) => {
          zipVars(
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

    case (env, MethodCallNode(invocant, method_name, args)) => {
      val invocant_object = i.evaluate(env, invocant)
      invocant_object match {
        case obj: MoeObject => {
          val klass = obj.getAssociatedClass.getOrElse(throw new MoeErrors.ClassNotFound("__CLASS__"))
          val meth = klass.getMethod(method_name).getOrElse(
            klass.getSubMethod(method_name).getOrElse(
              throw new MoeErrors.MethodNotFound(method_name)
            )
          )

          val evaluated_args = args.map(i.evaluate(env, _))

          i.pushCallStack(new MoeStackFrame(meth, evaluated_args, env, Some(obj))) 
          val result = obj.callMethod(meth, evaluated_args)
          i.popCallStack
          result
        }
        case _ => throw new MoeErrors.MoeException("Object expected")
      }
    }

  }
}