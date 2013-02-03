package org.moe.ast

import org.moe.runtime._

import scala.util.parsing.json._

class AST {
  def serialize: Any = throw new Exception("Could not serialize AST")
}

// AST containers

case class CompilationUnitNode(body: ScopeNode) extends AST {
  override def serialize = JSONObject(Map("CompilationUnitNode" -> body.serialize))
}
case class ScopeNode(body: StatementsNode) extends AST {
  override def serialize =
    JSONObject(Map("ScopeNode" -> body.serialize))
}

case class StatementsNode(nodes: List[AST]) extends AST {
  override def serialize = JSONObject(Map("StatementsNode" -> JSONArray(nodes.map(_.serialize))))
}

// literals

/**
 * A literal Int node
 *
 * @param value The Int value of this node
 */
case class IntLiteralNode(value: Int) extends AST {
  override def serialize = JSONObject(Map("IntLiteralNode" -> value))
}
case class FloatLiteralNode(value: Double) extends AST {
  override def serialize = JSONObject(Map("FloatLiteralNode" -> value))
}
case class StringLiteralNode(value: String) extends AST {
  override def serialize = JSONObject(Map("StringLiteralNode" -> value))
}
case class BooleanLiteralNode(value: Boolean) extends AST {
  override def serialize = JSONObject(Map("BooleanLiteralNode" -> value))
}

case class UndefLiteralNode() extends AST {
  override def serialize = "UndefLiteralNode"
}

case class SelfLiteralNode() extends AST {
  override def serialize = "SelfLiteralNode"
}
case class ClassLiteralNode() extends AST {
  override def serialize = "ClassLiteralNode"
}
case class SuperLiteralNode() extends AST {
  override def serialize = "SuperLiteralNode"
}

case class PairLiteralNode(key: AST, value: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "PairLiteralNode" -> JSONObject(
        Map(
          "key"   -> key.serialize,
          "value" -> value.serialize
        )
      )
    )
  )
}


case class ArrayLiteralNode(values: List[AST]) extends AST {
  override def serialize = JSONObject(Map("ArrayLiteralNode" -> JSONArray(values.map(_.serialize))))
}
case class HashLiteralNode(map: List[AST]) extends AST {
  override def serialize = JSONObject(Map("HashLiteralNode" -> JSONArray(map.map(_.serialize))))
}

// unary operators

case class IncrementNode(receiver: AST) extends AST {
  override def serialize = JSONObject(Map("IncrementNode" -> receiver.serialize))
}
case class DecrementNode(receiver: AST) extends AST {
  override def serialize = JSONObject(Map("DecrementNode" -> receiver.serialize))
}
case class NotNode(receiver: AST) extends AST {
  override def serialize = JSONObject(Map("NotNode" -> receiver.serialize))
}


// binary operators

case class AndNode(lhs: AST, rhs: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "AndNode" -> JSONObject(
        Map(
          "lhs" -> lhs.serialize,
          "rhs" -> rhs.serialize
        )
      )
    )
  )
}
case class OrNode(lhs: AST, rhs: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "OrNode" -> JSONObject(
        Map(
          "lhs" -> lhs.serialize,
          "rhs" -> rhs.serialize
        )
      )
    )
  )
}
case class LessThanNode(lhs: AST, rhs: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "LessThanNode" -> JSONObject(
        Map(
          "lhs" -> lhs.serialize,
          "rhs" -> rhs.serialize
        )
      )
    )
  )
}
case class GreaterThanNode(lhs: AST, rhs: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "GreaterThanNode" -> JSONObject(
        Map(
          "lhs" -> lhs.serialize,
          "rhs" -> rhs.serialize
        )
      )
    )
  )
}

// value lookup, assignment and declaration

case class ClassAccessNode(name: String) extends AST {
  override def serialize =
    JSONObject(Map("ClassAccessNode" -> name))
}
case class ClassDeclarationNode(name: String, superclass: String, body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "ClassDeclarationNode" -> JSONObject(
        Map(
          "name"       -> name,
          "superclass" -> superclass,
          "body"       -> body.serialize
        )
      )
    )
  )
}
case class PackageDeclarationNode(name: String, body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "PackageDeclarationNode" -> JSONObject(
        Map(
          "name" -> name,
          "body" -> body.serialize
        )
      )
    )
  )
}

case class ConstructorDeclarationNode(params: List[String], body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "ConstructorDeclarationNode" -> JSONObject(
        Map(
          "params" -> JSONArray(params),
          "body"   -> body.serialize
        )
      )
    )
  )
}
case class DestructorDeclarationNode(params: List[String], body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "DestructorDeclarationNode" -> JSONObject(
        Map(
          "params" -> JSONArray(params),
          "body"   -> body.serialize
        )
      )
    )
  )
}

case class MethodDeclarationNode(name: String, params: List[String], body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "MethodDeclarationNode" -> JSONObject(
        Map(
          "name"   -> name,
          "params" -> JSONArray(params),
          "body"   -> body.serialize
        )
      )
    )
  )
}
case class SubroutineDeclarationNode(name: String, params: List[String], body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "SubroutineDeclarationNode" -> JSONObject(
        Map(
          "name" -> name,
          "params" -> JSONArray(params),
          "body" -> body.serialize
        )
      )
    )
  )
}

case class AttributeAccessNode(name: String) extends AST {
  override def serialize = JSONObject(Map("AttributeAccessNode" -> name))
}
case class AttributeAssignmentNode(name: String, expression: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "AttributeAssignmentNode" -> JSONObject(
        Map(
          "name" -> name,
          "expression" -> expression.serialize
        )
      )
    )
  )
}
case class AttributeDeclarationNode(name: String, expression: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "AttributeDeclarationNode" -> JSONObject(
        Map(
          "name"       -> name,
          "expression" -> expression.serialize
        )
      )
    )
  )
}

case class VariableAccessNode(name: String) extends AST {
  override def serialize = JSONObject(Map("VariableAccessNode" -> name))
}
case class VariableAssignmentNode(name: String, expression: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "VariableAssignmentNode" -> JSONObject(
        Map(
          "name"       -> name,
          "expression" -> expression.serialize
        )
      )
    )
  )
}
case class VariableDeclarationNode(name: String, expression: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "VariableDeclarationNode" -> JSONObject(
        Map(
          "name" -> name,
          "expression" -> expression.serialize
        )
      )
    )
  )
}

case class HashValueAccessNode(hashName: String, key: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "HashValueAccessNode" -> JSONObject(
        Map(
          "hashname" -> hashName,
          "key"      -> key.serialize
        )
      )
    )
  )
}
case class ArrayElementAccessNode(arrayName: String, index: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "ArrayElementAccessNode" -> JSONObject(
        Map(
          "arrayname" -> arrayName,
          "index"     -> index.serialize
        )
      )
    )
  )
}

// operations

case class MethodCallNode(invocant: AST, method_name: String, args: List[AST]) extends AST {
  override def serialize = JSONObject(
    Map(
      "MethodCallNode" -> JSONObject(
        Map(
          "invocant"    -> invocant.serialize,
          "method_name" -> method_name,
          "args"        -> JSONArray(args.map(_.serialize))
        )
      )
    )
  )
}
case class SubroutineCallNode(function_name: String, args: List[AST]) extends AST {
  override def serialize = JSONObject(
    Map(
      "SubroutineCallNode" -> JSONObject(
        Map(
          "function_name" -> function_name,
          "args"          -> JSONArray(args.map(_.serialize))
        )
      )
    )
  )
}

// statements

case class IfNode(if_condition: AST, if_body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "IfNode" -> JSONObject(
        Map(
          "if_condition" -> if_condition.serialize,
          "if_body"      -> if_body.serialize
        )
      )
    )
  )
}
case class IfElseNode(if_condition: AST, if_body: AST, else_body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "IfElseNode" -> JSONObject(
        Map(
          "if_condition" -> if_condition.serialize,
          "if_body"      -> if_body.serialize,
          "else_body"    -> else_body.serialize
        )
      )
    )
  )
}
case class IfElsifNode(
  if_condition: AST,
  if_body: AST,
  elsif_condition: AST,
  elsif_body: AST)
  extends AST {
  override def serialize = JSONObject(
    Map(
      "IfElsifNode" -> JSONObject(
        Map(
          "if_condition"    -> if_condition.serialize,
          "if_body"         -> if_body.serialize,
          "elsif_condition" -> elsif_condition.serialize,
          "elsif_body"      -> elsif_body.serialize
        )
      )
    )
  )
}
case class IfElsifElseNode(
  if_condition: AST,
  if_body: AST,
  elsif_condition: AST,
  elsif_body: AST,
  else_body: AST)
  extends AST {
  override def serialize = JSONObject(
    Map(
      "IfElsifElseNode" -> JSONObject(
        Map(
          "if_condition"    -> if_condition.serialize,
          "if_body"         -> if_body.serialize,
          "elsif_condition" -> elsif_condition.serialize,
          "elsif_body"      -> elsif_body.serialize,
          "else_body"       -> else_body.serialize
        )
      )
    )
  )
}

case class UnlessNode(unless_condition: AST, unless_body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "UnlessNode" -> JSONObject(
        Map(
          "unless_condition" -> unless_condition.serialize,
          "unless_body"      -> unless_body.serialize
        )
      )
    )
  )
}
case class UnlessElseNode(unless_condition: AST, unless_body: AST, else_body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "UnlessElseNode" -> JSONObject(
        Map(
          "unless_condition" -> unless_condition.serialize,
          "unless_body"      -> unless_body.serialize,
          "else_body"        -> else_body.serialize
        )
      )
    )
  )
}
// TODO: UnlessElsif and UnlessElsifElse to match If above?
// Or should the normalization be handled by the parser instead? -TRS

case class TryNode(
  body: AST,
  catch_nodes: List[CatchNode],
  finally_nodes: List[FinallyNode])
  extends AST {
  override def serialize = JSONObject(
    Map(
      "TryNode" -> JSONObject(
        Map(
          "body"          -> body.serialize,
          "catch_nodes"   -> JSONArray(catch_nodes.map(_.serialize)),
          "finally_nodes" -> JSONArray(finally_nodes.map(_.serialize))
        )
      )
    )
  )
}
case class CatchNode(type_name: String, local_name: String, body: AST) extends AST {
  override def serialize = JSONObject(
    Map(
      "CatchNode" -> JSONObject(
        Map(
          "type_name"  -> type_name,
          "local_name" -> local_name,
          "body"       -> body.serialize
        )
      )
    )
  )
}
case class FinallyNode(body: AST) extends AST {
  override def serialize = JSONObject(Map("FinallyNode" -> body.serialize))
}

case class WhileNode(condition: AST, body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "WhileNode" -> JSONObject(
        Map(
          "condition" -> condition.serialize,
          "body"      -> body.serialize
        )
      )
    )
  )
}
case class DoWhileNode(condition: AST, body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "DoWhileNode" -> JSONObject(
        Map(
          "condition" -> condition.serialize,
          "body"      -> body.serialize
        )
      )
    )
  )
}

case class ForeachNode(topic: AST, list: AST, body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "ForeachNode" -> JSONObject(
        Map(
          "topic" -> topic.serialize,
          "list"  -> list.serialize,
          "body"  -> body.serialize
        )
      )
    )
  )
}
case class ForNode(init: AST, condition: AST, update: AST, body: StatementsNode) extends AST {
  override def serialize = JSONObject(
    Map(
      "ForNode" -> JSONObject(
        Map(
          "init"      -> init.serialize,
          "condition" -> condition.serialize,
          "update"    -> update.serialize,
          "body"      -> body.serialize
        )
      )
    )
  )
}
