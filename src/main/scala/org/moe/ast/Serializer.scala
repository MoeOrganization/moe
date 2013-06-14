package org.moe.ast
import org.moe.runtime._
import scala.util.parsing.json._

object Serializer {
  def toJSON(ast: AST): Any = ast match {
    case CompilationUnitNode(body) => JSONObject(Map("CompilationUnitNode" -> toJSON(body)))
    case ScopeNode(body)           => JSONObject(Map("ScopeNode" -> toJSON(body)))
    case StatementsNode(nodes)     => JSONObject(Map("StatementsNode" -> JSONArray(nodes.map(toJSON(_)))))

    case IntLiteralNode(value)     => JSONObject(Map("IntLiteralNode" -> value))
    case FloatLiteralNode(value)   => JSONObject(Map("FloatLiteralNode" -> value))
    case StringLiteralNode(value)  => JSONObject(Map("StringLiteralNode" -> value))
    case BooleanLiteralNode(value) => JSONObject(Map("BooleanLiteralNode" -> value.toString))

    case UndefLiteralNode() => "UndefLiteralNode"
    case SelfLiteralNode()  => "SelfLiteralNode"
    case ClassLiteralNode() => "ClassLiteralNode"
    case SuperCallNode() => "SuperCallNode"

    case RegexLiteralNode(value) => JSONObject(Map("RegexLiteralNode" -> value.toString))

    case PairLiteralNode(key, value) => JSONObject(
      Map(
        "PairLiteralNode" -> JSONObject(
          Map(
            "key"   -> toJSON(key),
            "value" -> toJSON(value)
          )
        )
      )
    )

    case ArrayLiteralNode(values)     => JSONObject(Map("ArrayLiteralNode" -> JSONArray(values.map(toJSON(_)))))
    case HashLiteralNode(map)         => JSONObject(Map("HashLiteralNode" -> JSONArray(map.map(toJSON(_)))))
    case RangeLiteralNode(start, end) => JSONObject(
      Map(
        "RangeLiteralNode" -> JSONObject(
          Map(
            "start" -> toJSON(start),
            "end"   -> toJSON(end)
          )
        )
      )
    )

    case CodeLiteralNode(signature, body) => JSONObject(
      Map(
        "CodeLiteralNode" -> JSONObject(
          Map(
            "signature" -> toJSON(signature),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case PrefixUnaryOpNode(lhs, operator) => JSONObject(
      Map(
        "PrefixUnaryOpNode" -> JSONObject(
          Map(
            "lhs"      -> toJSON(lhs),
            "operator" -> operator
          )
        )
      )
    )

    case PostfixUnaryOpNode(lhs, operator) => JSONObject(
      Map(
        "PostfixUnaryOpNode" -> JSONObject(
          Map(
            "lhs"      -> toJSON(lhs),
            "operator" -> operator
          )
        )
      )
    )

    case BinaryOpNode(lhs, operator, rhs) => JSONObject(
      Map(
        "BinaryOpNode" -> JSONObject(
          Map(
            "lhs"      -> toJSON(lhs),
            "operator" -> operator,
            "rhs"      -> toJSON(rhs)
          )
        )
      )
    )

    case TernaryOpNode(condExpr, trueExpr, falseExpr) => JSONObject(
      Map(
        "TernaryOpNode" -> JSONObject(
          Map(
            "condition" -> toJSON(condExpr),
            "trueExpr"  -> toJSON(trueExpr),
            "falseExpr" -> toJSON(falseExpr)
          )
        )
      )
    )

    case ShortCircuitBinaryOpNode(lhs, operator, rhs) => JSONObject(
      Map(
        "ShortCircuitBinaryOpNode" -> JSONObject(
          Map(
            "lhs"      -> toJSON(lhs),
            "operator" -> operator,
            "rhs"      -> toJSON(rhs)
          )
        )
      )
    )

    case ClassAccessNode(name) => JSONObject(Map("ClassAccessNode" -> name))

    case ClassDeclarationNode(name, superclass, body, version, authority) => JSONObject(
      Map(
        "ClassDeclarationNode" -> JSONObject(
          Map(
            "name"       -> name,
            "version"    -> version.getOrElse(""),
            "authority"  -> authority.getOrElse(""),
            "superclass" -> superclass.getOrElse(""),
            "body"       -> toJSON(body)
          )
        )
      )
    )

    case PackageDeclarationNode(name, body, version, authority) => JSONObject(
      Map(
        "PackageDeclarationNode" -> JSONObject(
          Map(
            "name"      -> name,
            "version"   -> version.getOrElse(""),
            "authority" -> authority.getOrElse(""),            
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case ParameterNode(name, optional, slurpy, named, Some(default)) => JSONObject(
      Map(
        "name"     -> name, 
        "optional" -> optional.toString, 
        "slurpy"   -> slurpy.toString,
        "named"    -> named.toString,
        "default"  -> toJSON(default)
      )
    )
    case ParameterNode(name, optional, slurpy, named, None) => JSONObject(
      Map(
        "name"     -> name, 
        "optional" -> optional.toString, 
        "slurpy"   -> slurpy.toString,
        "named"    -> named.toString
      )
    )
    case SignatureNode(params) => JSONArray(params.map(toJSON(_)))

    case SubMethodDeclarationNode(name, signature, body) => JSONObject(
      Map(
        "SubMethodDeclarationNode" -> JSONObject(
          Map(
            "name"      -> name,
            "signature" -> toJSON(signature),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case MethodDeclarationNode(name, signature, body) => JSONObject(
      Map(
        "MethodDeclarationNode" -> JSONObject(
          Map(
            "name"      -> name,
            "signature" -> toJSON(signature),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case SubroutineDeclarationNode(name, signature, body, traits) => JSONObject(
      Map(
        "SubroutineDeclarationNode" -> JSONObject(
          Map(
            "name"      -> name,
            "signature" -> toJSON(signature),
            "body"      -> toJSON(body),
            "traits"    -> traits.map(_.mkString(", ")).getOrElse("")
          )
        )
      )
    )

    case AttributeNameNode(name)   => JSONObject(Map("AttributeNameNode" -> name))
    case AttributeAccessNode(name) => JSONObject(Map("AttributeAccessNode" -> name))
    case AttributeAssignmentNode(name, expression) => JSONObject(
      Map(
        "AttributeAssignmentNode" -> JSONObject(
          Map(
            "name" -> name,
            "expression" -> toJSON(expression)
          )
        )
      )
    )
    case MultiAttributeAssignmentNode(names, expressions) => JSONObject(
      Map(
        "MultiAttributeAssignmentNode" -> JSONObject(
          Map(
            "names"       -> names.mkString(", "),
            "expressions" -> List(expressions.map(toJSON(_)))
          )
        )
      )
    )
    case AttributeDeclarationNode(name, expression) => JSONObject(
      Map(
        "AttributeDeclarationNode" -> JSONObject(
          Map(
            "name"       -> name,
            "expression" -> toJSON(expression)
          )
        )
      )
    )

    case VariableNameNode(name)   => JSONObject(Map("VariableNameNode" -> name))
    case VariableAccessNode(name) => JSONObject(Map("VariableAccessNode" -> name))
    case VariableAssignmentNode(name, expression) => JSONObject(
      Map(
        "VariableAssignmentNode" -> JSONObject(
          Map(
            "name"       -> name,
            "expression" -> toJSON(expression)
          )
        )
      )
    )
    case MultiVariableAssignmentNode(names, expressions) => JSONObject(
      Map(
        "MultiVariableAssignmentNode" -> JSONObject(
          Map(
            "names"       -> names.mkString(", "),
            "expressions" -> List(expressions.map(toJSON(_)))
          )
        )
      )
    )

    case VariableDeclarationNode(name, expression) => JSONObject(
      Map(
        "VariableDeclarationNode" -> JSONObject(
          Map(
            "name" -> name,
            "expression" -> toJSON(expression)
          )
        )
      )
    )

    case HashElementNameNode(hashName, keys) => JSONObject(
      Map(
        "HashElementNameNode" -> JSONObject(
          Map(
            "hashname" -> hashName,
            if (keys.length == 1)
              "key"    -> toJSON(keys.head)
            else
              "keys"   -> JSONArray(keys.map(toJSON(_)))
          )
        )
      )
    )
    case HashElementAccessNode(hashName, keys) => JSONObject(
      Map(
        "HashElementAccessNode" -> JSONObject(
          Map(
            "hashname" -> hashName,
            if (keys.length == 1)
              "key"    -> toJSON(keys.head)
            else
              "keys"   -> JSONArray(keys.map(toJSON(_)))
          )
        )
      )
    )
    case HashElementLvalueNode(hashName, keys, value) => JSONObject(
      Map(
        "HashElementLvalueNode" -> JSONObject(
          Map(
            "hashname" -> hashName,
            if (keys.length == 1)
              "key"    -> toJSON(keys.head)
            else
              "keys"   -> JSONArray(keys.map(toJSON(_))),
            "value"    -> toJSON(value)
          )
        )
      )
    )

    case ArrayElementNameNode(arrayName, indices) => JSONObject(
      Map(
        "ArrayElementNameNode" -> JSONObject(
          Map(
            "arrayname" -> arrayName,
            if (indices.length == 1)
              "index"   -> toJSON(indices.head)
            else
              "indices" -> JSONArray(indices.map(toJSON(_)))
          )
        )
      )
    )
    case ArrayElementAccessNode(arrayName, indices) => JSONObject(
      Map(
        "ArrayElementAccessNode" -> JSONObject(
          Map(
            "arrayname" -> arrayName,
            if (indices.length == 1)
              "index"   -> toJSON(indices.head)
            else
              "indices" -> JSONArray(indices.map(toJSON(_)))
          )
        )
      )
    )
    case ArrayElementLvalueNode(arrayName, indices, value) => JSONObject(
      Map(
        "ArrayElementLvalueNode" -> JSONObject(
          Map(
            "arrayname" -> arrayName,
            if (indices.length == 1)
              "index"   -> toJSON(indices.head)
            else
              "indices" -> JSONArray(indices.map(toJSON(_))),
            "value"     -> toJSON(value)
          )
        )
      )
    )

    case MethodCallNode(invocant, method_name, args) => JSONObject(
      Map(
        "MethodCallNode" -> JSONObject(
          Map(
            "invocant"    -> toJSON(invocant),
            "method_name" -> method_name,
            "args"        -> JSONArray(args.map(toJSON(_)))
          )
        )
      )
    )
    case SubroutineCallNode(function_name, args) => JSONObject(
      Map(
        "SubroutineCallNode" -> JSONObject(
          Map(
            "function_name" -> function_name,
            "args"          -> JSONArray(args.map(toJSON(_)))
          )
        )
      )
    )

    case UseStatement(name) => JSONObject(Map("UseStatement" -> name))

    case IfStruct (condition, body, None)  => JSONObject(
      Map(
        "IfStruct" -> JSONObject(
          Map(
            "condition" -> toJSON(condition),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case IfStruct (condition, body, Some(else_node))  => JSONObject(
      Map(
        "IfStruct" -> JSONObject(
          Map(
            "condition" -> toJSON(condition),
            "body"      -> toJSON(body),
            "else_node" -> toJSON(else_node)
          )
        )
      )
    )

    case IfNode(if_node) => JSONObject(
      Map(
        "IfNode" -> JSONObject(
          Map(
            "if_node" -> toJSON(if_node)
          )
        )
      )
    )

    case UnlessNode(unless_node) => JSONObject(
      Map(
        "UnlessNode" -> JSONObject(
          Map(
            "unless_node" -> toJSON(unless_node)
          )
        )
      )
    )

    case TryNode(body, catch_nodes, finally_nodes) => JSONObject(
      Map(
        "TryNode" -> JSONObject(
          Map(
            "body"          -> toJSON(body),
            "catch_nodes"   -> JSONArray(catch_nodes.map(toJSON(_))),
            "finally_nodes" -> JSONArray(finally_nodes.map(toJSON(_)))
          )
        )
      )
    )
    case CatchNode(type_name, local_name, body) => JSONObject(
      Map(
        "CatchNode" -> JSONObject(
          Map(
            "type_name"  -> type_name,
            "local_name" -> local_name,
            "body"       -> toJSON(body)
          )
        )
      )
    )
    case FinallyNode(body) => JSONObject(Map("FinallyNode" -> toJSON(body)))

    case WhileNode(condition, body) => JSONObject(
      Map(
        "WhileNode" -> JSONObject(
          Map(
            "condition" -> toJSON(condition),
            "body"      -> toJSON(body)
          )
        )
      )
    )
    case DoWhileNode(condition, body) => JSONObject(
      Map(
        "DoWhileNode" -> JSONObject(
          Map(
            "condition" -> toJSON(condition),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case ForeachNode(topic, list, body) => JSONObject(
      Map(
        "ForeachNode" -> JSONObject(
          Map(
            "topic" -> toJSON(topic),
            "list"  -> toJSON(list),
            "body"  -> toJSON(body)
          )
        )
      )
    )
    case ForNode(init, condition, update, body) => JSONObject(
      Map(
        "ForNode" -> JSONObject(
          Map(
            "init"      -> toJSON(init),
            "condition" -> toJSON(condition),
            "update"    -> toJSON(update),
            "body"      -> toJSON(body)
          )
        )
      )
    )

    case RegexMatchNode(target, pattern, flags) => JSONObject(
      Map(
        "RegexMatchNode" -> JSONObject(
          Map(
            "target"  -> toJSON(target),
            "pattern" -> toJSON(pattern),
            "flags"   -> toJSON(flags)
          )
        )
      )
    )

    case RegexSubstNode(target, pattern, replacement, flags) => JSONObject(
      Map(
        "RegexSubstNode" -> JSONObject(
          Map(
            "target"      -> toJSON(target),
            "pattern"     -> toJSON(pattern),
            "replacement" -> toJSON(replacement),
            "flags"       -> toJSON(flags)
          )
        )
      )
    )

    case MatchExpressionNode(pattern, flags) => JSONObject(
      Map(
        "MatchExpressionNode" -> JSONObject(
          Map(
            "pattern" -> toJSON(pattern),
            "flags"   -> toJSON(flags)
          )
        )
      )
    )

    case SubstExpressionNode(pattern, replacement, flags) => JSONObject(
      Map(
        "SubstExpressionNode" -> JSONObject(
          Map(
            "pattern"     -> toJSON(pattern),
            "replacement" -> toJSON(replacement),
            "flags"       -> toJSON(flags)
          )
        )
      )
    )

    case TransExpressionNode(search, replacement, flags) => JSONObject(
      Map(
        "TransExpressionNode" -> JSONObject(
          Map(
            "search"      -> toJSON(search),
            "replacement" -> toJSON(replacement),
            "flags"       -> toJSON(flags)
          )
        )
      )
    )
    case TransOpNode(target, search, replacement, flags) => JSONObject(
      Map(
        "TransOpNode" -> JSONObject(
          Map(
            "target"      -> toJSON(target),
            "search"      -> toJSON(search),
            "replacement" -> toJSON(replacement),
            "flags"       -> toJSON(flags)
          )
        )
      )
    )

    case StringSequenceNode(strParts) => JSONObject(
      Map(
        "StringSequenceNode" -> JSONObject(
          Map(
            "stringParts" -> JSONArray(strParts.map(toJSON(_)))
          )
        )
      )
    )

    case EvalExpressionNode(value)   => JSONObject(Map("EvalExpressionNode" -> value))
    case ExecuteCommandNode(command) => JSONObject(Map("ExecuteCommandNode" -> toJSON(command)))

    case x => "stub: " + x
  }

  // thanks to https://gist.github.com/umitanuki/944839
  // modified to make the output a bit more compact where appropriate
  def pprint(j: Option[Any], l: Int = 0):String = {

    def is_simple_object(x: Option[Any]): Boolean = {
      x match {
        case Some(o: JSONObject) => if (o.obj.keys.size > 1)
                                      false
                                    else
                                      o.obj.head match {
                                        case (_, v: JSONObject) => false
                                        case (_, v: JSONArray)  => false
                                        case _                  => true
                                      }
        case Some(a: JSONArray)  => if (a.list.size > 1) false else is_simple_object(Some(a.list.head))
        case _                   => true
      }
    }

    val indent = (for(i <- List.range(0, l)) yield "  ").mkString
    j match{
      case Some(o: JSONObject) => {
        val max_key_len = o.obj.keys.map(key => key.length).max
        def padded(key: String) =
          "\"" + key + "\"" + (if (key.length < max_key_len) " ".padTo(max_key_len - key.length, ' ') else "")

        if (is_simple_object(Some(o)))
          "{ " + o.obj.keys.map(key => padded(key) + " : " + pprint(o.obj.get(key), l + 1)).mkString(",\n") +  " }"
        else
          List("{",
               o.obj.keys.map(key => indent + "  " + padded(key) + " : " + pprint(o.obj.get(key), l + 1)).mkString(",\n"),
               indent + "}").mkString("\n")
      }
      case Some(a: JSONArray) => {
        List("[",
             a.list.map(v => indent + "  " + pprint(Some(v), l + 1)).mkString(",\n"),
             indent + "]").mkString("\n")
      }
      case Some(s: String) => "\"" + s + "\""
      case Some(n: Number) => n.toString
      case None => "null"
      case _ => "undefined"
    }
  }

  def toJSONPretty(ast: AST): String = pprint(Some(toJSON(ast)))
}
