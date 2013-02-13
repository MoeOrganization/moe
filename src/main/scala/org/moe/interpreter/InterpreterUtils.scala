package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

object InterpreterUtils {
  def objToNumeric(obj: MoeObject): Double = obj match {
    case i: MoeIntObject => i.getNativeValue.toDouble
    case n: MoeFloatObject => n.getNativeValue
    case _ => throw new MoeErrors.MoeException("Could not coerce object into numeric")
  }

  def objToInteger(obj: MoeObject): Int = obj match {
    case i: MoeIntObject => i.getNativeValue
    case n: MoeFloatObject => n.getNativeValue.toInt
    case _ => throw new MoeErrors.MoeException("Could not coerce object into integer")
  }

  def objToString(obj: MoeObject): String = obj match {
    case i: MoeIntObject => i.getNativeValue.toString
    case n: MoeFloatObject => n.getNativeValue.toString
    case s: MoeStringObject => s.getNativeValue
    case _ => throw new MoeErrors.MoeException("Could not coerce object into string")
  }

  def inNewEnv[T](env: MoeEnvironment)(body: MoeEnvironment => T): T = {
    val newEnv = new MoeEnvironment(Some(env))

    body(env)
  }

  def walkAST(ast: AST, callback: (AST, Int) => Unit, level: Int = 0): Unit = {
    callback(ast, level)
    val walknext: (AST) => Unit = walkAST(_, callback, level + 1)
    ast match {
      case CompilationUnitNode(body) => walknext(body)
      case ScopeNode(body)           => walknext(body)
      case StatementsNode(nodes)     => nodes.foreach(walknext(_))

      case PairLiteralNode(key, value) => {
        walknext(key)
        walknext(value)
      }

      case ArrayLiteralNode(values)    => values.foreach(walknext(_))
      case HashLiteralNode(map)        => map.foreach(walknext(_))

      case IncrementNode(receiver, _)     => walknext(receiver)
      case DecrementNode(receiver, _)     => walknext(receiver)
      case NotNode(receiver)           => walknext(receiver)

      case AndNode(lhs, rhs) => {
        walknext(lhs)
        walknext(rhs)
      }
      case OrNode(lhs, rhs) => {
        walknext(lhs)
        walknext(rhs)
      }
      case LessThanNode(lhs, rhs) => {
        walknext(lhs)
        walknext(rhs)
      }
      case GreaterThanNode(lhs, rhs) => {
        walknext(lhs)
        walknext(rhs)
      }

      case ClassDeclarationNode(name, superclass, body) => walknext(body)

      case PackageDeclarationNode(_, body) => walknext(body)
      case ConstructorDeclarationNode(_, body) => walknext(body)
      case DestructorDeclarationNode(_, body) => walknext(body)
      case MethodDeclarationNode(_, _, body) => walknext(body)
      case SubroutineDeclarationNode(_, _, body) => walknext(body)

      case AttributeAssignmentNode(name, expression) => walknext(expression)
      case AttributeDeclarationNode(name, expression) => walknext(expression)

      case VariableAssignmentNode(name, expression) => walknext(expression)
      case VariableDeclarationNode(name, expression) => walknext(expression)

      case HashValueAccessNode(hashName, key) => walknext(key)
      case ArrayElementAccessNode(arrayName, index) => walknext(index)
      // ^ Maybe we need to walk VariableAccessNode(arrayName)? Not sure.

      case MethodCallNode(invocant, method_name, args) => {
        walknext(invocant)
        args.foreach(walknext(_))
      }
      case SubroutineCallNode(method_name, args) => {
        args.foreach(walknext(_))
      }

      case IfNode(if_condition, if_body) => {
        walknext(if_condition)
        walknext(if_body)
      }
      case IfElseNode(if_condition, if_body, else_body) => {
        walknext(if_condition)
        walknext(if_body)
        walknext(else_body)
      }
      case IfElsifNode(if_condition, if_body, elsif_condition, elsif_body) => {
        walknext(if_condition)
        walknext(if_body)
        walknext(elsif_condition)
        walknext(elsif_body)
      }
      case IfElsifElseNode(if_condition, if_body, elsif_condition, elsif_body, else_body) => {
        walknext(if_condition)
        walknext(if_body)
        walknext(elsif_condition)
        walknext(elsif_body)
        walknext(else_body)
      }

      case UnlessNode(unless_condition, unless_body) => {
        walknext(unless_condition)
        walknext(unless_body)
      }
      case UnlessElseNode(unless_condition, unless_body, else_body) => {
        walknext(unless_condition)
        walknext(unless_body)
        walknext(else_body)
      }

      case TryNode(body, catch_nodes, finally_nodes) => {
        walknext(body)
        catch_nodes.foreach(walknext(_))
        finally_nodes.foreach(walknext(_))
      }
      case CatchNode(_, _, body) => {
        walknext(body)
      }
      case FinallyNode(body) => walknext(body)

      case WhileNode(condition, body) => {
        walknext(condition)
        walknext(body)
      }
      case DoWhileNode(condition, body) => {
        walknext(body)
        walknext(condition)
      }

      case ForeachNode(topic, list, body) => {
        walknext(topic)
        walknext(list)
        walknext(body)
      }
      case ForNode(init, condition, update, body) => {
        walknext(init)
        walknext(condition)
        walknext(update)
        walknext(body)
      }
      case _ => return
    }
  }

  // Throw an exception if a variable isn't closed over at declaration time
  // This is to prevent variables in the same env but after declaration getting
  // sucked into the closure and causing unexpected behavior.
  def throwForUndeclaredVars(env: MoeEnvironment, params: List[String], body: StatementsNode): Unit = {
      var declared: Set[String] = params.toSet
      walkAST(
        body,
        { (ast: AST, _) =>
          ast match {
            case VariableDeclarationNode(varname, _) =>
              declared += varname
            case VariableAccessNode(varname) =>
              if (!env.has(varname) && !declared(varname)) {
                throw new MoeErrors.VariableNotFound(varname)
              }
            case _ => Unit
          }
        }
      )
  }
}

