package org.moe.ast

import org.moe.runtime._

abstract class AST

// AST containers

case class CompilationUnitNode(body: ScopeNode) extends AST
case class ScopeNode(body: StatementsNode) extends AST

case class StatementsNode(nodes: List[AST]) extends AST

// literals

/**
 * A literal Int node
 *
 * @param value The Int value of this node
 */
case class IntLiteralNode(value: Int) extends AST
case class FloatLiteralNode(value: Double) extends AST
case class StringLiteralNode(value: String) extends AST
case class BooleanLiteralNode(value: Boolean) extends AST

case class UndefLiteralNode() extends AST

case class SelfLiteralNode() extends AST
case class ClassLiteralNode() extends AST
case class SuperLiteralNode() extends AST

case class PairLiteralNode(key: AST, value: AST) extends AST


case class ArrayLiteralNode(values: List[AST]) extends AST
case class HashLiteralNode(map: List[AST]) extends AST

// unary operators

case class IncrementNode(receiver: AST) extends AST
case class DecrementNode(receiver: AST) extends AST
case class NotNode(receiver: AST) extends AST


// binary operators

case class AndNode(lhs: AST, rhs: AST) extends AST
case class OrNode(lhs: AST, rhs: AST) extends AST
case class LessThanNode(lhs: AST, rhs: AST) extends AST
case class GreaterThanNode(lhs: AST, rhs: AST) extends AST

// value lookup, assignment and declaration

case class ClassAccessNode(name: String) extends AST
case class ClassDeclarationNode(name: String, superclass: String, body: AST) extends AST
case class PackageDeclarationNode(name: String, body: StatementsNode) extends AST

case class ConstructorDeclarationNode(params: List[String], body: AST) extends AST
case class DestructorDeclarationNode(params: List[String], body: AST) extends AST

case class MethodDeclarationNode(name: String, params: List[String], body: AST) extends AST
case class SubroutineDeclarationNode(name: String, params: List[String], body: StatementsNode) extends AST

case class AttributeAccessNode(name: String) extends AST
case class AttributeAssignmentNode(name: String, expression: AST) extends AST
case class AttributeDeclarationNode(name: String, expression: AST) extends AST

case class VariableAccessNode(name: String) extends AST
case class VariableAssignmentNode(name: String, expression: AST) extends AST
case class VariableDeclarationNode(name: String, expression: AST) extends AST

// operations

case class MethodCallNode(invocant: AST, method_name: String, args: List[AST]) extends AST
case class SubroutineCallNode(function_name: String, args: List[AST]) extends AST

// statements

case class IfNode(if_condition: AST, if_body: AST) extends AST
case class IfElseNode(if_condition: AST, if_body: AST, else_body: AST) extends AST
case class IfElsifNode(
  if_condition: AST,
  if_body: AST,
  elsif_condition: AST,
  elsif_body: AST)
  extends AST
case class IfElsifElseNode(
  if_condition: AST,
  if_body: AST,
  elsif_condition: AST,
  elsif_body: AST,
  else_body: AST)
  extends AST

case class UnlessNode(unless_condition: AST, unless_body: AST) extends AST
case class UnlessElseNode(unless_condition: AST, unless_body: AST, else_body: AST) extends AST
// TODO: UnlessElsif and UnlessElsifElse to match If above?
// Or should the normalization be handled by the parser instead? -TRS

case class TryNode(
  body: AST,
  catch_nodes: List[CatchNode],
  finally_nodes: List[FinallyNode])
  extends AST
case class CatchNode(type_name: String, local_name: String, body: AST) extends AST
case class FinallyNode(body: AST) extends AST

case class WhileNode(condition: AST, body: StatementsNode) extends AST
case class DoWhileNode(condition: AST, body: StatementsNode) extends AST

case class ForeachNode(topic: AST, list: AST, body: StatementsNode) extends AST
case class ForNode(init: AST, condition: AST, update: AST, body: StatementsNode) extends AST
