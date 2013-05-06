package org.moe.interpreter.guts

import org.moe.runtime._
import org.moe.ast._

trait Utils {

  def getCurrentPackage (env: MoeEnvironment): MoePackage = env.getCurrentPackage.getOrElse(
    throw new MoeErrors.PackageNotFound("__PACKAGE__")
  )

  def getCurrentClass (env: MoeEnvironment): MoeClass = env.getCurrentClass.getOrElse(
    throw new MoeErrors.ClassNotFound("__CLASS__")
  )

  def callMethod(invocant: MoeObject, method: String, args: List[MoeObject], klass: String = null) =
    invocant.callMethod(
      invocant.getAssociatedClass.getOrElse(
        throw new MoeErrors.ClassNotFound(Option(klass).getOrElse(invocant.getClassName))
      ).getMethod(method).getOrElse(
        throw new MoeErrors.MethodNotFound("method " + method + "> missing in class " + Option(klass).getOrElse(invocant.getClassName))
      ),
      args
    )

  def zipVars (r: MoeRuntime, names: List[String], expressions: List[MoeObject], f: ((String, MoeObject)) => Unit): Unit = {
    if (expressions.isEmpty) {
      names.foreach(f(_, r.NativeObjects.getUndef)) 
    } else if (names.isEmpty) {
      ()
    } else {
      f(names.head, expressions.headOption.getOrElse(r.NativeObjects.getUndef))
      zipVars(r, names.tail, expressions.tail, f)
    }
  }

  // Throw an exception if a variable isn't closed over at declaration time
  // This is to prevent variables in the same env but after declaration getting
  // sucked into the closure and causing unexpected behavior.
  def throwForUndeclaredVars(env: MoeEnvironment, signature: MoeSignature, body: StatementsNode): Unit = {
      var declared: Set[String] = signature.getParams.map(_.getName).toSet
      walkAST(
        body,
        { ast: AST =>
          ast match {
            case VariableDeclarationNode(varname, _) =>
              declared += varname
            case VariableAccessNode(varname) =>
              if (!env.has(varname) && !declared(varname) && !env.isSpecialMarker(varname)) {
                throw new MoeErrors.VariableNotFound(varname)
              }
            case _ => Unit
          }
        }
      )
  }

  // XXX - this no longer captures all the AST nodes anymore
  def walkAST(ast: AST, callback: (AST) => Unit): Unit = {
    callback(ast)
    ast match {
      case CompilationUnitNode(body) => walkAST(body, callback)
      case ScopeNode(body, _)        => walkAST(body, callback)
      case StatementsNode(nodes)     => nodes.foreach(walkAST(_, callback))

      case PairLiteralNode(key, value) => {
        walkAST(key, callback)
        walkAST(value, callback)
      }

      case ArrayLiteralNode(values)    => values.foreach(walkAST(_, callback))
      case HashLiteralNode(map)        => map.foreach(walkAST(_, callback))

      case PrefixUnaryOpNode(receiver, _)     => walkAST(receiver, callback)
      case PostfixUnaryOpNode(receiver, _)    => walkAST(receiver, callback)

      case BinaryOpNode(lhs, _, rhs) => {
        walkAST(lhs, callback)
        walkAST(rhs, callback)
      }

      case ClassDeclarationNode(name, superclass, body, _, _) => walkAST(body, callback)

      case PackageDeclarationNode(_, body, _, _) => walkAST(body, callback)
      case SubMethodDeclarationNode(_, _, body) => walkAST(body, callback)
      case MethodDeclarationNode(_, _, body) => walkAST(body, callback)
      case SubroutineDeclarationNode(_, _, body, _) => walkAST(body, callback)

      case AttributeAssignmentNode(name, expression) => walkAST(expression, callback)
      case AttributeDeclarationNode(name, expression) => walkAST(expression, callback)

      case VariableAssignmentNode(name, expression) => walkAST(expression, callback)
      case VariableDeclarationNode(name, expression) => walkAST(expression, callback)

      case HashElementAccessNode(hashName, keys) => keys.foreach(walkAST(_, callback))
      case ArrayElementAccessNode(arrayName, indices) => indices.foreach(walkAST(_, callback))
      // ^ Maybe we need to walk VariableAccessNode(arrayName)? Not sure.

      case MethodCallNode(invocant, method_name, args) => {
        walkAST(invocant, callback)
        args.foreach(walkAST(_, callback))
      }
      case SubroutineCallNode(method_name, args) => {
        args.foreach(walkAST(_, callback))
      }

      case IfNode(if_node) => {
        walkAST(if_node, callback)
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


}

