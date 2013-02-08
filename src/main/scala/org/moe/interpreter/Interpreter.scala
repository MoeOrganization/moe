package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

import scala.collection.mutable.HashMap

class Interpreter {

  import InterpreterUtils._

  val stub = new MoeObject()

  def eval(runtime: MoeRuntime, env: MoeEnvironment, node: AST): MoeObject = {
    val scoped = inNewEnv[MoeObject](env) _
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
        nodes.foldLeft[MoeObject](runtime.NativeObjects.getUndef)(
          (_, node) => eval(runtime, env, node)
        )
      }

      // literals

      case IntLiteralNode(value)     => runtime.NativeObjects.getInt(value)
      case FloatLiteralNode(value)   => runtime.NativeObjects.getFloat(value)
      case StringLiteralNode(value)  => runtime.NativeObjects.getString(value)
      case BooleanLiteralNode(value) => runtime.NativeObjects.getBool(value)

      case UndefLiteralNode() => runtime.NativeObjects.getUndef
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
        runtime.NativeObjects.getArray(array)
      }

      case ArrayElementAccessNode(arrayName: String, index: AST) => {
        val index_result = eval(runtime, env, index)
        val array_value = env.get(arrayName) match {
          case Some(a: MoeArrayObject) => a.getNativeValue
          case _ => throw new MoeErrors.UnexpectedType("MoeArrayObject expected")
        }

        // TODO: ListBuffer probably, like stevan said - JM
        var native_index = objToInteger(index_result)
        while (native_index < 0) {
          native_index += array_value.size
        }
        try {
          array_value(native_index)
        }
        catch {
          case _: java.lang.IndexOutOfBoundsException => runtime.NativeObjects.getUndef // TODO: warn
        }
      }

      case PairLiteralNode(key, value) => runtime.NativeObjects.getPair(eval(runtime, env, key) -> eval(runtime, env, value))

      case HashLiteralNode(map) => {
        // NOTE:
        // forcing each element to become
        // a single MoePairObject might be
        // a little too restrictive, it
        // should (in theory) be possible
        // for it to evaluate into many
        // MoePairObject instances as well
        // - SL
        runtime.NativeObjects.getHash(
          map.map(
            pair => eval(runtime, env, pair)
            .asInstanceOf[MoePairObject].getNativeValue
          ).toMap
        )
      }

      case HashValueAccessNode(hashName: String, key: AST) => {
        val key_result = eval(runtime, env, key)
        val hash_map = env.get(hashName) match {
          case Some(h: MoeHashObject) => h.getNativeValue
          case _ => throw new MoeErrors.UnexpectedType("MoeHashObject expected")
        }

        hash_map.get(objToString(key_result))
          .getOrElse(runtime.NativeObjects.getUndef)
      }

      case RangeLiteralNode(start, end) => {
        val range_start  = Utils.objToInteger(eval(runtime, env, start))
        val range_end    = Utils.objToInteger(eval(runtime, env, end))
        val range: Range = new Range(range_start, range_end + 1, 1)
        val array: List[MoeObject] = range.toList.map(runtime.NativeObjects.getInt(_))
        runtime.NativeObjects.getArray(array)
      }

      // unary operators

      case IncrementNode(receiver: AST) => receiver match {
        case VariableAccessNode(varName) => env.get(varName).getOrElse(
          throw new MoeErrors.VariableNotFound(varName)
        ) match {
          case i: MoeIntObject => {
            env.set(varName, runtime.NativeObjects.getInt(i.getNativeValue + 1)).get
          }
          case n: MoeFloatObject => {
            env.set(varName, runtime.NativeObjects.getFloat(n.getNativeValue + 1.0)).get
          }
        }
      }
      case DecrementNode(receiver: AST) => receiver match {
        case VariableAccessNode(varName) => env.get(varName).getOrElse(
          throw new MoeErrors.VariableNotFound(varName)
        ) match {
          case i: MoeIntObject => {
            env.set(varName, runtime.NativeObjects.getInt(i.getNativeValue - 1)).get
          }
          case n: MoeFloatObject => {
            env.set(varName, runtime.NativeObjects.getFloat(n.getNativeValue - 1.0)).get
          }
        }
      }

      case NotNode(receiver) => {
        if(eval(runtime, env, receiver).isTrue) {
          runtime.NativeObjects.getFalse
        } else {
          runtime.NativeObjects.getTrue
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
        val lhs_result: Double = objToNumeric(eval(runtime, env, lhs))
        val rhs_result: Double = objToNumeric(eval(runtime, env, rhs))

        val result = lhs_result < rhs_result
        runtime.NativeObjects.getBool(result)
      }

      case GreaterThanNode(lhs, rhs) => {
        val lhs_result: Double = objToNumeric(eval(runtime, env, lhs))
        val rhs_result: Double = objToNumeric(eval(runtime, env, rhs))

        val result = lhs_result > rhs_result
        runtime.NativeObjects.getBool(result)
      }

      // value lookup, assignment and declaration

      case ClassAccessNode(name) => stub
      case ClassDeclarationNode(name, superclass, body) => stub

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

      case ConstructorDeclarationNode(params, body) => stub
      case DestructorDeclarationNode(params, body) => stub

      case MethodDeclarationNode(name, params, body) => stub
      // TODO: handle arguments
      case SubroutineDeclarationNode(name, params, body) => {
        scoped { sub_env =>
          var declared: Set[String] = params.toSet
          var closed_over: Set[String] = Set()
          walkAST(
            body,
            { ast: AST =>
              ast match {
                case VariableDeclarationNode(varname, _) =>
                  declared += varname
                case VariableAccessNode(varname) =>
                  if (env.has(varname) && !declared(varname)) {
                    closed_over += varname
                  }
                  else if (!declared(varname)) {
                    throw new MoeErrors.VariableNotFound(varname)
                  }
                case _ => Unit
              }
            }
          )
          val sub = new MoeSubroutine(
            name,
            args => {
              // TODO make a run through after parse-time to check
              // for argument counts at the call sites
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

      case AttributeAccessNode(name) => stub
      case AttributeAssignmentNode(name, expression) => stub
      case AttributeDeclarationNode(name, expression) => stub

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

      case MethodCallNode(invocant, method_name, args) => stub
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
          runtime.NativeObjects.getUndef // XXX
        }
      }

      case DoWhileNode(condition, body) => {
        scoped { newEnv =>
          do {
            eval(runtime, newEnv, body)
          } while (eval(runtime, newEnv, condition).isTrue)
          runtime.NativeObjects.getUndef // XXX
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
              runtime.NativeObjects.getUndef // XXX
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
            runtime.NativeObjects.getUndef
          }
        )

      }
      case _ => throw new MoeErrors.UnknownNode("Unknown Node")
    }
  }

}
