package org.moe.interpreter

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._

class Interpreter {

  import InterpreterUtils._

  val stub = new MoeObject()

  def eval(runtime: MoeRuntime, env: MoeEnvironment, node: AST): MoeObject = {

    // no need to do all that typing ..
    import runtime.NativeObjects._

    val scoped = inNewEnv[MoeObject](env) _

    // interpret ..
    node match {

      // containers

      case CompilationUnitNode(body) => eval(runtime, env, body)
      case ScopeNode(body) => eval(runtime, new MoeEnvironment(Some(env)), body)
      case StatementsNode(nodes) => {

        // foldLeft iterates over each node (left to right) in the list, executing
        // a function.  That function is given two arguments: the result of the
        // previous iteration and the next item in the list.  It returns the result
        // of the final iteration. Many times it used to accumulate, such as
        // finding a sum of a list.  In thise case we don't acculate, we just
        // return the result of each eval.  Therefore the final result will be
        // the result of the last eval.
        nodes.foldLeft[MoeObject](getUndef)(
          (_, node) => eval(runtime, env, node)
        )
      }

      // literals

      case IntLiteralNode(value)     => getInt(value)
      case FloatLiteralNode(value)   => getNum(value)
      case StringLiteralNode(value)  => getStr(value)
      case BooleanLiteralNode(value) => getBool(value)

      case UndefLiteralNode() => getUndef
      case SelfLiteralNode()  => env.getCurrentInvocant.getOrElse(
          throw new MoeErrors.InvocantNotFound("__SELF__")
        )
      case ClassLiteralNode() => env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
      case SuperLiteralNode() => {
        val klass = env.getCurrentClass
        klass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        ).getSuperclass.getOrElse(
          throw new MoeErrors.SuperclassNotFound(klass.get.getName)
        )
      }

      case ArrayLiteralNode(values) => getArray(values.map(eval(runtime, env, _)):_*)

      case ArrayElementAccessNode(arrayName: String, index: AST) => {
        val index_result = eval(runtime, env, index)
        val array_value = env.get(arrayName) match {
          case Some(a: MoeArrayObject) => a
          case _ => throw new MoeErrors.UnexpectedType("MoeArrayObject expected")
        }

        array_value.callMethod(
          array_value.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound("Array")
          ).getMethod("postcircumfix:<[]>").getOrElse(
            throw new MoeErrors.MethodNotFound("postcircumfix:<[]>")
          ), 
          List(index_result)
        )
      }

      case PairLiteralNode(key, value) => getPair(
        eval(runtime, env, key).unboxToString.get -> eval(runtime, env, value)
      )

      case HashLiteralNode(values) => getHash(values.map(eval(runtime, env, _).unboxToTuple.get):_*)

      case HashElementAccessNode(hashName: String, key: AST) => {
        val key_result = eval(runtime, env, key)
        val hash_map = env.get(hashName) match {
          case Some(h: MoeHashObject) => h
          case _ => throw new MoeErrors.UnexpectedType("MoeHashObject expected")
        }

        hash_map.callMethod(
          hash_map.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound("Hash")
          ).getMethod("postcircumfix:<{}>").getOrElse(
            throw new MoeErrors.MethodNotFound("postcircumfix:<{}>")
          ), 
          List(key_result)
        )
      }

      case RangeLiteralNode(start, end) => {
        val s = eval(runtime, env, start)
        val e = eval(runtime, env, end)

        var result: List[MoeObject] = List();

        (s, e) match {
          case (s: MoeIntObject, e: MoeIntObject) => {
            val range_start  = s.unboxToInt.get
            val range_end    = e.unboxToInt.get
            val range: Range = new Range(range_start, range_end + 1, 1)
            result = range.toList.map(getInt(_))
          }
          case (s: MoeStrObject, e: MoeStrObject) => {
            val range_start = s.unboxToString.get
            val range_end   = e.unboxToString.get

            if (range_start.length <= range_end.length) {
              var elems: List[String] = List()
              var str = range_start
              while (str <= range_end || str.length < range_end.length) {
                elems = elems :+ str
                str = MoeUtil.magicalStringIncrement(str)
              }
              result = elems.map(getStr(_))
            }
          }
          case _ => throw new MoeErrors.UnexpectedType("Pair of MoeIntObject or MoeStrObject expected")
        }

        getArray(result:_*)
      }

      // unary operators

      case PrefixUnaryOpNode(lhs: AST, operator: String) => {
        val receiver = eval(runtime, env, lhs)
        receiver.callMethod(
          receiver.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound(receiver.toString)
          ).getMethod("prefix:<" + operator + ">").getOrElse(
            throw new MoeErrors.MethodNotFound("method prefix:<" + operator + "> missing in class " + receiver.getClassName)
          ),
          List()
        )
      }

      case PostfixUnaryOpNode(lhs: AST, operator: String) => {
        val receiver = eval(runtime, env, lhs)
        receiver.callMethod(
          receiver.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound(receiver.toString)
          ).getMethod("postfix:<" + operator + ">").getOrElse(
            throw new MoeErrors.MethodNotFound("method postfix:<" + operator + "> missing in class " + receiver.getClassName)
          ), 
          List()
        )
      }

      // binary operators

      case BinaryOpNode(lhs: AST, operator: String, rhs: AST) => {
        val receiver = eval(runtime, env, lhs)
        val arg      = eval(runtime, env, rhs)
        receiver.callMethod(
          receiver.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound(receiver.toString)
          ).getMethod("infix:<" + operator + ">").getOrElse(
            throw new MoeErrors.MethodNotFound("method infix:<" + operator + "> missing in class " + receiver.getClassName)
          ), 
          List(arg)
        )
      }

      // short circuit binary operators
      // rhs is not evaluated, unless it is necessary

      case ShortCircuitBinaryOpNode(lhs: AST, operator: String, rhs: AST) => {
        val receiver = eval(runtime, env, lhs)
        val arg      = new MoeLazyEval(this, runtime, env, rhs)
        receiver.callMethod(
          receiver.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound(receiver.toString)
          ).getMethod("infix:<" + operator + ">").getOrElse(
            throw new MoeErrors.MethodNotFound("method infix:<" + operator + "> missing in class " + receiver.getClassName)
          ), 
          List(arg)
        )
      }

      // ternary operator

      case TernaryOpNode(cond: AST, trueExpr: AST, falseExpr: AST) => {
        val receiver = eval(runtime, env, cond)
        val argTrue  = new MoeLazyEval(this, runtime, env, trueExpr)
        val argFalse = new MoeLazyEval(this, runtime, env, falseExpr)
        receiver.callMethod(
          receiver.getAssociatedClass.getOrElse(
            throw new MoeErrors.ClassNotFound(receiver.toString)
          ).getMethod("infix:<?:>").getOrElse(
            throw new MoeErrors.MethodNotFound("method infix:<?:> missing in class " + receiver.getClassName)
          ),
          List(argTrue, argFalse)
        )
      }

      // value lookup, assignment and declaration

      case ClassAccessNode(name) => runtime.lookupClass(
        name, 
        env.getCurrentPackage.getOrElse(
          throw new MoeErrors.PackageNotFound("__PACKAGE__")
        )
      ).getOrElse( 
        throw new MoeErrors.ClassNotFound(name) 
      )

      case ClassDeclarationNode(name, superclass, body) => {
        val pkg = env.getCurrentPackage.getOrElse(
          throw new MoeErrors.PackageNotFound("__PACKAGE__")
        )

        val superclass_class: Option[MoeClass] = superclass.map(
          runtime.lookupClass(_, pkg).getOrElse(
            throw new MoeErrors.ClassNotFound(superclass.getOrElse(""))
          )
        )

        val klass = new MoeClass(
          name, None, None, Some(superclass_class.getOrElse(runtime.getClassClass))
        )

        pkg.addClass(klass)

        scoped { klass_env =>
          klass_env.setCurrentClass(klass)
          eval(runtime, klass_env, body)
          klass
        }

        klass
      }

      case PackageDeclarationNode(name, body) => {
        scoped { newEnv =>
          val parent = env.getCurrentPackage.getOrElse(
            throw new MoeErrors.PackageNotFound("__PACKAGE__")
          )
          val pkg    = new MoePackage(name, newEnv)
          parent.addSubPackage(pkg)
          newEnv.setCurrentPackage(pkg)
          val result = eval(runtime, newEnv, body)
          newEnv.setCurrentPackage(parent)
          result
        }
      }

      case ParameterNode(name, optional, slurpy, named) => (optional, slurpy, named) match {
        case (false, false, false) => new MoePositionalParameter(name)
        case (true,  false, false) => new MoeOptionalParameter(name)
        case (false, true,  false) => new MoeSlurpyParameter(name)
        case (false, false, true)  => new MoeNamedParameter(name)
        case (false, true,  true)  => new MoeSlurpyNamedParameter(name)
        case _                     => throw new MoeErrors.InvalidParameter("parameter must be one of slurpy, optional or named")
      }

      case SignatureNode(params) => new MoeSignature(
        params.map(eval(runtime, env, _).asInstanceOf[MoeParameter])
      )

      case ConstructorDeclarationNode(signature, body) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val sig = eval(runtime, env, signature).asInstanceOf[MoeSignature]
        throwForUndeclaredVars(env, sig, body)

        val constructor = new MoeMethod(
          name            = "BUILD",
          signature       = sig,
          declaration_env = env,
          body            = (e) => eval(runtime, e, body)
        )
        klass.setConstructor(Some(constructor))

        val new_method = new MoeMethod(
          name            = "new",
          signature       = sig,
          declaration_env = env,
          body            = {
            (env) => 
              val c = env.getCurrentInvocant.get.asInstanceOf[MoeClass]
              val i = c.newInstance.asInstanceOf[MoeOpaque]
              c.collectAllAttributes.foreach(a => a._2.getDefault.map(i.setValue(a._1, _)))

              // call BUILD (this really should be a BUILDALL method)
              val e = new MoeEnvironment(Some(env))
              e.setCurrentInvocant(i)
              c.getConstructor.map(_.executeBody(e))

              i
          }
        )
        klass.addMethod(new_method)
        
        constructor
      }

      case DestructorDeclarationNode(signature, body) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val sig = eval(runtime, env, signature).asInstanceOf[MoeSignature]
        throwForUndeclaredVars(env, sig, body)
        val destructor = new MoeMethod(
          name            = "DEMOLISH",
          signature       = sig,
          declaration_env = env,
          body            = (e) => eval(runtime, e, body)
        )
        klass.setDestructor(Some(destructor))
        destructor
      }

      case MethodDeclarationNode(name, signature, body) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val sig = eval(runtime, env, signature).asInstanceOf[MoeSignature]
        throwForUndeclaredVars(env, sig, body)
        val method = new MoeMethod(
          name            = name,
          signature       = sig,
          declaration_env = env,
          body            = (e) => eval(runtime, e, body)
        )
        klass.addMethod(method)
        method
      }

      case SubroutineDeclarationNode(name, signature, body) => {
        val sig = eval(runtime, env, signature).asInstanceOf[MoeSignature]
        throwForUndeclaredVars(env, sig, body)
        val sub = new MoeSubroutine(
          name            = name,
          signature       = sig,
          declaration_env = env,
          body            = (e) => eval(runtime, e, body)
        )
        env.getCurrentPackage.getOrElse(
          throw new MoeErrors.PackageNotFound("__PACKAGE__")
        ).addSubroutine( sub )
        sub
      }
      
      case AttributeAccessNode(name) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val attr = klass.getAttribute(name).getOrElse(
          throw new MoeErrors.AttributeNotFound(name)
        )
        val invocant = env.getCurrentInvocant
        invocant match {
          case Some(invocant: MoeOpaque) => invocant.getValue(name).getOrElse(
            throw new MoeErrors.InstanceValueNotFound(name)
          )
          case _ => throw new MoeErrors.UnexpectedType(invocant.getOrElse("(undef)").toString)
        }
      }
      case AttributeAssignmentNode(name, expression) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val attr = klass.getAttribute(name).getOrElse(
          throw new MoeErrors.AttributeNotFound(name)
        )
        val expr = eval(runtime, env, expression)
        env.getCurrentInvocant match {
          case Some(invocant: MoeOpaque) => invocant.setValue(name, expr)
                                            // XXX attr."setDefault"(expr) ?
          case Some(invocant)            => throw new MoeErrors.UnexpectedType(invocant.toString)
          case None                      => throw new MoeErrors.MoeException("Attribute default already declared")
        }
        expr
      }
      case AttributeDeclarationNode(name, expression) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        val attr_default = () => eval(runtime, env, expression)
        val attr = new MoeAttribute(name, Some(attr_default))
        klass.addAttribute(attr)
        attr
      }

      // TODO context etc
      case VariableAccessNode(name) => env.get(name).getOrElse(
          throw new MoeErrors.VariableNotFound(name)
        )
      case VariableAssignmentNode(name, expression) => {
        env.set(name, eval(runtime, env, expression)).getOrElse(
          throw new MoeErrors.VariableNotFound(name)
        )
      }
      case VariableDeclarationNode(name, expression) => {
        env.create(name, eval(runtime, env, expression)).get
      }

      // operations
      case MethodCallNode(invocant, method_name, args) => {
        val invocant_object = eval(runtime, env, invocant)
        invocant_object match {
          case obj: MoeObject =>
            val klass = obj.getAssociatedClass.getOrElse(
              throw new MoeErrors.ClassNotFound("__CLASS__")
            )
            val meth = klass.getMethod(method_name).getOrElse(
              throw new MoeErrors.MethodNotFound(method_name)
            )
            obj.callMethod(meth, args.map(eval(runtime, env, _)))
          case _ => throw new MoeErrors.MoeException("Object expected")
        }
      }

      case SubroutineCallNode(function_name, args) => {
        val sub = runtime.lookupSubroutine(
          function_name, 
          env.getCurrentPackage.getOrElse(
            throw new MoeErrors.PackageNotFound("__PACKAGE__")
          )
        ).getOrElse( 
          throw new MoeErrors.SubroutineNotFound(function_name)
        )

        sub.execute(new MoeArguments(args.map(eval(runtime, env, _))))
      }

      // statements

      case IfNode(if_node) => {
        if (eval(runtime, env, if_node.condition).isTrue) {
          eval(runtime, env, if_node.body)
        } else if (if_node.else_node.isDefined) {
          eval(runtime, env, IfNode(if_node.else_node.get))
        } else {
          getUndef
        }
      }
          
      case UnlessNode(unless_condition, unless_body) => {
        eval(runtime, env,
          UnlessElseNode(
            unless_condition,
            unless_body,
            UndefLiteralNode()
          )
        )
      }
      case UnlessElseNode(unless_condition, unless_body, else_body) => {
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
        eval(runtime, env, IfNode(if_node))
      }

      case TryNode(body, catch_nodes, finally_nodes) => stub
      case CatchNode(type_name, local_name, body) => stub
      case FinallyNode(body) => stub

      case WhileNode(condition, body) => {
        scoped { newEnv =>
          while (eval(runtime, newEnv, condition).isTrue) {
            eval(runtime, newEnv, body)
          }
          getUndef // XXX
        }
      }

      case DoWhileNode(condition, body) => {
        scoped { newEnv =>
          do {
            eval(runtime, newEnv, body)
          } while (eval(runtime, newEnv, condition).isTrue)
          getUndef // XXX
        }
      }

      case ForeachNode(topic, list, body) => {
        eval(runtime, env, list) match {
          case objects: MoeArrayObject => {
            val applyScopeInjection = {
              (
                newEnv: MoeEnvironment, 
                name: String, 
                obj: MoeObject, 
                f: (MoeEnvironment, String, MoeObject) => Any
              ) =>
              f(env, name, obj)
              eval(runtime, newEnv, body)
            }

            scoped { newEnv =>
              for (o <- objects.getNativeValue) // XXX - fix this usage of getNativeValue
                topic match {
                  // XXX ran into issues trying to eval(runtime, env, ScopeNode(...))
                  // since o is already evaluated at this point
                  case VariableDeclarationNode(name, expr) =>
                    applyScopeInjection(newEnv, name, o, (_.create(_, _)))
                  // Don't do anything special here, env access will just walk back
                  case VariableAccessNode(name) =>
                    applyScopeInjection(newEnv, name, o, (_.set(_, _)))
                }
              getUndef // XXX
            }
          }
        }
      }
      case ForNode(init, condition, update, body) => {
        scoped(
          newEnv => {
            eval(runtime, newEnv, init)
            while (eval(runtime, newEnv, condition).isTrue) {
              eval(runtime, newEnv, body)
              eval(runtime, newEnv, update)
            }
            getUndef
          }
        )

      }
      case _ => throw new MoeErrors.UnknownNode("Unknown Node")
    }
  }

}
