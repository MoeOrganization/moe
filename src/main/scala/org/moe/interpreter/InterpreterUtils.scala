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

  def walkAST(ast: AST, callback: (AST) => Unit): Unit = {
    callback(ast)
    ast match {
      case CompilationUnitNode(body) => walkAST(body, callback)
      case ScopeNode(body)           => walkAST(body, callback)
      case StatementsNode(nodes)     => nodes.foreach(walkAST(_, callback))

      case PairLiteralNode(key, value) => {
        walkAST(key, callback)
        walkAST(value, callback)
      }

      case ArrayLiteralNode(values)    => values.foreach(walkAST(_, callback))
      case HashLiteralNode(map)        => map.foreach(walkAST(_, callback))

      case IncrementNode(receiver, _)     => walkAST(receiver, callback)
      case DecrementNode(receiver, _)     => walkAST(receiver, callback)
      case NotNode(receiver)           => walkAST(receiver, callback)

      case AndNode(lhs, rhs) => {
        walkAST(lhs, callback)
        walkAST(rhs, callback)
      }
      case OrNode(lhs, rhs) => {
        walkAST(lhs, callback)
        walkAST(rhs, callback)
      }
      case LessThanNode(lhs, rhs) => {
        walkAST(lhs, callback)
        walkAST(rhs, callback)
      }
      case GreaterThanNode(lhs, rhs) => {
        walkAST(lhs, callback)
        walkAST(rhs, callback)
      }

      case ClassDeclarationNode(name, superclass, body) => walkAST(body, callback)

      case PackageDeclarationNode(_, body) => walkAST(body, callback)
      case ConstructorDeclarationNode(_, body) => walkAST(body, callback)
      case DestructorDeclarationNode(_, body) => walkAST(body, callback)
      case MethodDeclarationNode(_, _, body) => walkAST(body, callback)
      case SubroutineDeclarationNode(_, _, body) => walkAST(body, callback)

      case AttributeAssignmentNode(name, expression) => walkAST(expression, callback)
      case AttributeDeclarationNode(name, expression) => walkAST(expression, callback)

      case VariableAssignmentNode(name, expression) => walkAST(expression, callback)
      case VariableDeclarationNode(name, expression) => walkAST(expression, callback)

      case HashElementAccessNode(hashName, key) => walkAST(key, callback)
      case ArrayElementAccessNode(arrayName, index) => walkAST(index, callback)
      // ^ Maybe we need to walk VariableAccessNode(arrayName)? Not sure.

      case MethodCallNode(invocant, method_name, args) => {
        walkAST(invocant, callback)
        args.foreach(walkAST(_, callback))
      }
      case SubroutineCallNode(method_name, args) => {
        args.foreach(walkAST(_, callback))
      }

      case IfNode(if_condition, if_body) => {
        walkAST(if_condition, callback)
        walkAST(if_body, callback)
      }
      case IfElseNode(if_condition, if_body, else_body) => {
        walkAST(if_condition, callback)
        walkAST(if_body, callback)
        walkAST(else_body, callback)
      }
      case IfElsifNode(if_condition, if_body, elsif_condition, elsif_body) => {
        walkAST(if_condition, callback)
        walkAST(if_body, callback)
        walkAST(elsif_condition, callback)
        walkAST(elsif_body, callback)
      }
      case IfElsifElseNode(if_condition, if_body, elsif_condition, elsif_body, else_body) => {
        walkAST(if_condition, callback)
        walkAST(if_body, callback)
        walkAST(elsif_condition, callback)
        walkAST(elsif_body, callback)
        walkAST(else_body, callback)
      }

      case UnlessNode(unless_condition, unless_body) => {
        walkAST(unless_condition, callback)
        walkAST(unless_body, callback)
      }
      case UnlessElseNode(unless_condition, unless_body, else_body) => {
        walkAST(unless_condition, callback)
        walkAST(unless_body, callback)
        walkAST(else_body, callback)
      }

      case TryNode(body, catch_nodes, finally_nodes) => {
        walkAST(body, callback)
        catch_nodes.foreach(walkAST(_, callback))
        finally_nodes.foreach(walkAST(_, callback))
      }
      case CatchNode(_, _, body) => {
        walkAST(body, callback)
      }
      case FinallyNode(body) => walkAST(body, callback)

      case WhileNode(condition, body) => {
        walkAST(condition, callback)
        walkAST(body, callback)
      }
      case DoWhileNode(condition, body) => {
        walkAST(body, callback)
        walkAST(condition, callback)
      }

      case ForeachNode(topic, list, body) => {
        walkAST(topic, callback)
        walkAST(list, callback)
        walkAST(body, callback)
      }
      case ForNode(init, condition, update, body) => {
        walkAST(init, callback)
        walkAST(condition, callback)
        walkAST(update, callback)
        walkAST(body, callback)
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
        { ast: AST =>
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

