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
case class SuperCallNode() extends AST

case class PairLiteralNode(key: AST, value: AST) extends AST

case class ArrayLiteralNode(values: List[AST]) extends AST
case class HashLiteralNode(map: List[AST]) extends AST

case class CodeLiteralNode(signature: SignatureNode, body: StatementsNode) extends AST

case class RangeLiteralNode(start: AST, end: AST) extends AST

// unary operators

case class PrefixUnaryOpNode(lhs: AST, operator: String)  extends AST
case class PostfixUnaryOpNode(rhs: AST, operator: String) extends AST

// binary operators

case class BinaryOpNode(lhs: AST, operator: String, rhs: AST) extends AST
case class ShortCircuitBinaryOpNode(lhs: AST, operator: String, rhs: AST) extends AST

// ternary operator

case class TernaryOpNode(cond: AST, trueExpr: AST, falseExpr: AST) extends AST

// value lookup, assignment and declaration

case class ParameterNode(
    name     : String, 
    optional : Boolean     = false, 
    slurpy   : Boolean     = false, 
    named    : Boolean     = false,
    default  : Option[AST] = None
) extends AST 

case class SignatureNode(params: List[ParameterNode]) extends AST

case class ClassDeclarationNode(
  name       : String, 
  superclass : Option[String], 
  body       : StatementsNode,
  version    : Option[String] = None,
  authority  : Option[String] = None
) extends AST

case class PackageDeclarationNode(
  name      : String, 
  body      : StatementsNode,
  version   : Option[String] = None,
  authority : Option[String] = None  
) extends AST

case class SubMethodDeclarationNode(name: String, signature: SignatureNode, body: StatementsNode) extends AST
case class MethodDeclarationNode(name: String, signature: SignatureNode, body: StatementsNode) extends AST
case class SubroutineDeclarationNode(name: String, signature: SignatureNode, body: StatementsNode, traits: Option[List[String]] = None) extends AST
case class AttributeDeclarationNode(name: String, expression: AST) extends AST
case class VariableDeclarationNode(name: String, expression: AST) extends AST

case class AttributeNameNode(name: String) extends AST
case class VariableNameNode(name: String) extends AST
case class ArrayElementNameNode(arrayName: String, indices: List[AST]) extends AST
case class HashElementNameNode(hashName: String, indices: List[AST]) extends AST

case class ClassAccessNode(name: String) extends AST
case class AttributeAccessNode(name: String) extends AST
case class VariableAccessNode(name: String) extends AST
case class HashElementAccessNode(hashName: String, keys: List[AST]) extends AST
case class ArrayElementAccessNode(arrayName: String, indices: List[AST]) extends AST

// TODO - these should get converted to binary ops
case class AttributeAssignmentNode(name: String, expression: AST) extends AST
case class MultiAttributeAssignmentNode(names: List[String], expressions: List[AST]) extends AST
case class VariableAssignmentNode(name: String, expression: AST) extends AST
case class MultiVariableAssignmentNode(names: List[String], expressions: List[AST]) extends AST
case class ArrayElementLvalueNode(arrayName: String, indices: List[AST], expression: AST) extends AST
case class HashElementLvalueNode(hashName: String, keys: List[AST], expression: AST) extends AST

// operations

case class MethodCallNode(invocant: AST, method_name: String, args: List[AST]) extends AST
case class SubroutineCallNode(function_name: String, args: List[AST]) extends AST

// statements

case class UseStatement(name: String) extends AST

case class IfStruct (
  var condition: AST,
  var body: AST, 
  var else_node: Option[IfStruct] = None
) extends AST

case class IfNode(if_node: IfStruct) extends AST 

case class UnlessStruct(
  var condition: AST,
  var body: AST, 
  var else_node: Option[IfStruct] = None
) extends AST

case class UnlessNode(unless_node: UnlessStruct) extends AST

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

case class RegexLiteralNode(rx: String) extends AST
case class MatchExpressionNode(pattern: AST, flags: AST) extends AST
case class SubstExpressionNode(pattern: AST, replacement: AST, flags: AST) extends AST
case class RegexMatchNode(target: AST, pattern: AST, flags: AST) extends AST
case class RegexSubstNode(target: AST, pattern: AST, replacement: AST, flags: AST) extends AST
