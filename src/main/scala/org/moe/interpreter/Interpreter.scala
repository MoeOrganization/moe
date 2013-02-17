package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import scala.collection.mutable.HashMap

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
      case FloatLiteralNode(value)   => getFloat(value)
      case StringLiteralNode(value)  => getString(value)
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

      case ArrayLiteralNode(values) => {
        val array: List[MoeObject] = values.map(eval(runtime, env, _))
        getArray(array)
      }

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

      case PairLiteralNode(key, value) => getPair(eval(runtime, env, key) -> eval(runtime, env, value))

      case HashLiteralNode(map) => {
        // NOTE:
        // forcing each element to become
        // a single MoePairObject might be
        // a little too restrictive, it
        // should (in theory) be possible
        // for it to evaluate into many
        // MoePairObject instances as well
        // - SL
        getHash(
          map.map(
            pair => eval(runtime, env, pair)
            .asInstanceOf[MoePairObject].getNativeValue
          ).toMap
        )
      }

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
        (s, e) match {
          case (s: MoeIntObject, e: MoeIntObject) => {
            val range_start  = s.unboxToInt.get
            val range_end    = e.unboxToInt.get
            val range: Range = new Range(range_start, range_end + 1, 1)
            val array: List[MoeObject] = range.toList.map(getInt(_))
            getArray(array)
          }
          case (s: MoeStringObject, e: MoeStringObject) => {
            val range_start = s.unboxToString.get
            val range_end   = e.unboxToString.get

            if (range_start.length > range_end.length)
              getArray()
            else {
              var elems: List[String] = List()
              var str = range_start
              while (str <= range_end || str.length < range_end.length) {
                elems = elems :+ str
                str = magicalStringIncrement(str)
              }
              getArray(elems.map(getString(_)))
            }
          }
          case _ => throw new MoeErrors.UnexpectedType("Pair of MoeIntObject or MoeStringObject expected")
        }
      }

      // unary operators

      case IncrementNode(receiver: AST, is_prefix) => receiver match {
        case VariableAccessNode(varName) => env.get(varName).getOrElse(
          throw new MoeErrors.VariableNotFound(varName)
        ) match {
          case i: MoeIntObject => {
            val new_i = getInt(i.getNativeValue + 1)
            env.set(varName, new_i)
            if (is_prefix) new_i else i
          }
          case n: MoeFloatObject => {
            val new_n = getFloat(n.getNativeValue + 1.0)
            env.set(varName, new_n)
            if (is_prefix) new_n else n
          }
          case s: MoeStringObject => {
            val new_s = getString(magicalStringIncrement(s.getNativeValue))
            env.set(varName, new_s)
            if (is_prefix) new_s else s
          }
        }
      }
      case DecrementNode(receiver: AST, is_prefix) => receiver match {
        case VariableAccessNode(varName) => env.get(varName).getOrElse(
          throw new MoeErrors.VariableNotFound(varName)
        ) match {
          case i: MoeIntObject => {
            val new_i = getInt(i.getNativeValue - 1)
            env.set(varName, new_i)
            if (is_prefix) new_i else i
          }
          case n: MoeFloatObject => {
            val new_n = getFloat(n.getNativeValue - 1.0)
            env.set(varName, new_n)
            if (is_prefix) new_n else n
          }
        }
      }

      case NotNode(receiver) => {
        if(eval(runtime, env, receiver).isTrue) {
          getFalse
        } else {
          getTrue
        }
      }

      // binary operators

      case AndNode(lhs, rhs) => {
        val left_result = eval(runtime, env, lhs)
        if(left_result.isTrue) {
          eval(runtime, env, rhs)
        } else {
          left_result
        }
      }

      case OrNode(lhs, rhs) => {
        val left_result = eval(runtime, env, lhs)
        if(left_result.isTrue) {
          left_result
        } else {
          eval(runtime, env, rhs)
        }
      }

      case LessThanNode(lhs, rhs) => {
        val lhs_result: Double = eval(runtime, env, lhs).unboxToDouble.get
        val rhs_result: Double = eval(runtime, env, rhs).unboxToDouble.get

        val result = lhs_result < rhs_result
        getBool(result)
      }

      case GreaterThanNode(lhs, rhs) => {
        val lhs_result: Double = eval(runtime, env, lhs).unboxToDouble.get
        val rhs_result: Double = eval(runtime, env, rhs).unboxToDouble.get

        val result = lhs_result > rhs_result
        getBool(result)
      }

      // value lookup, assignment and declaration

      case ClassAccessNode(name) => {
        env.getCurrentPackage.getOrElse(
          throw new MoeErrors.PackageNotFound("__PACKAGE__")
        ).getClass(name).getOrElse(
          throw new MoeErrors.ClassNotFound(name)
        )
      }
      case ClassDeclarationNode(name, superclass, body) => {
        val pkg = env.getCurrentPackage.getOrElse(
          throw new MoeErrors.PackageNotFound("__PACKAGE__")
        )

        val superclass_class: Option[MoeClass] = superclass.map(
          pkg.getClass(_).getOrElse(
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
          eval(runtime, newEnv, body)
        }
      }

      // TODO: constructor overloading
      case ConstructorDeclarationNode(params, body) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        throwForUndeclaredVars(env, params, body)
        scoped { constructor_env =>
          val method = new MoeMethod(
            "new",
            (invocant, args) => {
              val param_pairs = params zip args
              param_pairs.foreach({ case (param, arg) =>
                constructor_env.create(param, arg)
              })
              val instance = klass.newInstance
              constructor_env.setCurrentInvocant(instance)
              eval(runtime, constructor_env, body)
              instance
            }
          )
          env.getCurrentClass.getOrElse(
            throw new MoeErrors.ClassNotFound("__CLASS__")
          ).addMethod(method)
          method
        }
      }
      case DestructorDeclarationNode(params, body) => stub

      case MethodDeclarationNode(name, params, body) => {
        val klass = env.getCurrentClass.getOrElse(
          throw new MoeErrors.ClassNotFound("__CLASS__")
        )
        throwForUndeclaredVars(env, params, body)
        scoped { method_env =>
          val method = new MoeMethod(
            name,
            (invocant, args) => {
              val param_pairs = params zip args
              param_pairs.foreach({ case (param, arg) =>
                method_env.create(param, arg)
              })
              method_env.setCurrentInvocant(invocant)
              eval(runtime, method_env, body)
            }
          )
          env.getCurrentClass.getOrElse(
            throw new MoeErrors.ClassNotFound("__CLASS__")
          ).addMethod(method)
          method
        }
      }
      case SubroutineDeclarationNode(name, params, body) => {
        throwForUndeclaredVars(env, params, body)
        scoped { sub_env =>
          val sub = new MoeSubroutine(
            name,
            args => {
              val param_pairs = params zip args
              param_pairs.foreach({ case (param, arg) =>
                sub_env.create(param, arg)
              })
              eval(runtime, sub_env, body)
            }
          )
          env.getCurrentPackage.getOrElse(
            throw new MoeErrors.PackageNotFound("__PACKAGE__")
          ).addSubroutine( sub )
          sub
        }
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
          case Some(invocant: MoeOpaque) =>
            invocant.getValue(name).getOrElse(attr.getDefault.getOrElse(getUndef))
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
        val attr_default = eval(runtime, env, expression)
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
            invocant_object.callMethod(meth, args.map(eval(runtime, env, _)))
          case _ => throw new MoeErrors.MoeException("Object expected")
        }
      }
      case SubroutineCallNode(function_name, args) => {
        val sub = env.getCurrentPackage.getOrElse(
            throw new MoeErrors.PackageNotFound("__PACKAGE__")
          ).getSubroutine(function_name).getOrElse(
            throw new MoeErrors.SubroutineNotFound(function_name)
        )
        // NOTE:
        // I think we might need to eval these
        // in the context of the sub body, and
        // we also need to make sure that we 
        // add the args to the environment as 
        // well.
        // - SL
        sub.execute(args.map(eval(runtime, env, _)))
      }

      // statements

      case IfNode(if_condition, if_body) => {
        eval(runtime, env,
          IfElseNode(
            if_condition,
            if_body,
            UndefLiteralNode()
          )
        )
      }

      case IfElseNode(if_condition, if_body, else_body) => {
        if (eval(runtime, env, if_condition).isTrue) {
          eval(runtime, env, if_body)
        } else {
          eval(runtime, env, else_body)
        }
      }

      case IfElsifNode(if_condition, if_body, elsif_condition, elsif_body) => {
        eval(runtime, env,
          IfElseNode(
            if_condition,
            if_body,
            IfNode(
              elsif_condition,
              elsif_body
            )
          )
        )
      }

      case IfElsifElseNode(if_condition, if_body, elsif_condition, elsif_body, else_body) => {
        eval(runtime, env,
          IfElseNode(
            if_condition,
            if_body,
            IfElseNode(
              elsif_condition,
              elsif_body,
              else_body
            )
          )
        )
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
        eval(runtime, env,
          IfElseNode(
            NotNode(unless_condition),
            unless_body,
            else_body
          )
        )
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
              for (o <- objects.getNativeValue)
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
