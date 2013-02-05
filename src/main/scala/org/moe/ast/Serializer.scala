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
    case BooleanLiteralNode(value) => JSONObject(Map("BooleanLiteralNode" -> value))

    case UndefLiteralNode() => "UndefLiteralNode"
    case ClassLiteralNode() => "ClassLiteralNode"
    case SuperLiteralNode() => "SuperLiteralNode"

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

    case ArrayLiteralNode(values)    => JSONObject(Map("ArrayLiteralNode" -> JSONArray(values.map(toJSON(_)))))
    case ArrayRefLiteralNode(values) => JSONObject(Map("ArrayRefLiteralNode" -> JSONArray(values.map(toJSON(_)))))
    case HashLiteralNode(map)        => JSONObject(Map("HashLiteralNode" -> JSONArray(map.map(toJSON(_)))))
    case HashRefLiteralNode(map)     => JSONObject(Map("HashRefLiteralNode" -> JSONArray(map.map(toJSON(_)))))
    case IncrementNode(receiver)     => JSONObject(Map("IncrementNode" -> toJSON(receiver)))
    case DecrementNode(receiver)     => JSONObject(Map("DecrementNode" -> toJSON(receiver)))
    case NotNode(receiver)           => JSONObject(Map("NotNode" -> toJSON(receiver)))

    case AndNode(lhs, rhs) => JSONObject(
      Map(
        "AndNode" -> JSONObject(
          Map(
            "lhs" -> toJSON(lhs),
            "rhs" -> toJSON(rhs)
          )
        )
      )
    )
    case OrNode(lhs, rhs) => JSONObject(
      Map(
        "OrNode" -> JSONObject(
          Map(
            "lhs" -> toJSON(lhs),
            "rhs" -> toJSON(rhs)
          )
        )
      )
    )

    case LessThanNode(lhs, rhs) => JSONObject(
      Map(
        "LessThanNode" -> JSONObject(
          Map(
            "lhs" -> toJSON(lhs),
            "rhs" -> toJSON(rhs)
          )
        )
      )
    )
    case GreaterThanNode(lhs, rhs) => JSONObject(
      Map(
        "GreaterThanNode" -> JSONObject(
          Map(
            "lhs" -> toJSON(lhs),
            "rhs" -> toJSON(rhs)
          )
        )
      )
    )

    case ClassAccessNode(name) => JSONObject(Map("ClassAccessNode" -> name))

    case ClassDeclarationNode(name, superclass, body) => JSONObject(
      Map(
        "ClassDeclarationNode" -> JSONObject(
          Map(
            "name"       -> name,
            "superclass" -> superclass,
            "body"       -> toJSON(body)
          )
        )
      )
    )

    case PackageDeclarationNode(name, body) => JSONObject(
      Map(
        "PackageDeclarationNode" -> JSONObject(
          Map(
            "name" -> name,
            "body" -> toJSON(body)
          )
        )
      )
    )

    case ConstructorDeclarationNode(params, body) => JSONObject(
      Map(
        "ConstructorDeclarationNode" -> JSONObject(
          Map(
            "params" -> JSONArray(params),
            "body"   -> toJSON(body)
          )
        )
      )
    )
    case DestructorDeclarationNode(params, body) => JSONObject(
        Map(
          "DestructorDeclarationNode" -> JSONObject(
            Map(
              "params" -> JSONArray(params),
              "body"   -> toJSON(body)
              )
            )
          )
        )

    case MethodDeclarationNode(name, params, body) => JSONObject(
        Map(
          "MethodDeclarationNode" -> JSONObject(
            Map(
              "name"   -> name,
              "params" -> JSONArray(params),
              "body"   -> toJSON(body)
              )
            )
          )
        )
    case SubroutineDeclarationNode(name, params, body) => JSONObject(
      Map(
        "SubroutineDeclarationNode" -> JSONObject(
          Map(
            "name" -> name,
            "params" -> JSONArray(params),
            "body" -> toJSON(body)
          )
        )
      )
    )

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

    case HashValueAccessNode(hashName, key) => JSONObject(
      Map(
        "HashValueAccessNode" -> JSONObject(
          Map(
            "hashname" -> hashName,
            "key"      -> toJSON(key)
          )
        )
      )
    )
    case ArrayElementAccessNode(arrayName, index) => JSONObject(
      Map(
        "ArrayElementAccessNode" -> JSONObject(
          Map(
            "arrayname" -> arrayName,
            "index"     -> toJSON(index)
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

    case IfNode(if_condition, if_body) => JSONObject(
      Map(
        "IfNode" -> JSONObject(
          Map(
            "if_condition" -> toJSON(if_condition),
            "if_body"      -> toJSON(if_body)
          )
        )
      )
    )
    case IfElseNode(if_condition, if_body, else_body) => JSONObject(
      Map(
        "IfElseNode" -> JSONObject(
          Map(
            "if_condition" -> toJSON(if_condition),
            "if_body"      -> toJSON(if_body),
            "else_body"    -> toJSON(else_body)
          )
        )
      )
    )
    case IfElsifNode(if_condition, if_body, elsif_condition, elsif_body) => JSONObject(
      Map(
        "IfElsifNode" -> JSONObject(
          Map(
            "if_condition"    -> toJSON(if_condition),
            "if_body"         -> toJSON(if_body),
            "elsif_condition" -> toJSON(elsif_condition),
            "elsif_body"      -> toJSON(elsif_body)
          )
        )
      )
    )
    case IfElsifElseNode(if_condition, if_body, elsif_condition, elsif_body, else_body) => JSONObject(
      Map(
        "IfElsifElseNode" -> JSONObject(
          Map(
            "if_condition"    -> toJSON(if_condition),
            "if_body"         -> toJSON(if_body),
            "elsif_condition" -> toJSON(elsif_condition),
            "elsif_body"      -> toJSON(elsif_body),
            "else_body"       -> toJSON(else_body)
          )
        )
      )
    )

    case UnlessNode(unless_condition, unless_body) => JSONObject(
      Map(
        "UnlessNode" -> JSONObject(
          Map(
            "unless_condition" -> toJSON(unless_condition),
            "unless_body"      -> toJSON(unless_body)
          )
        )
      )
    )
    case UnlessElseNode(unless_condition, unless_body, else_body) => JSONObject(
      Map(
        "UnlessElseNode" -> JSONObject(
          Map(
            "unless_condition" -> toJSON(unless_condition),
            "unless_body"      -> toJSON(unless_body),
            "else_body"        -> toJSON(else_body)
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

    case _ => "stub"
  }
}

