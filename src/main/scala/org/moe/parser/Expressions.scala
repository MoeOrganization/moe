package org.moe.parser

import ParserUtils._

import scala.util.parsing.combinator._
import org.moe.ast._

trait Expressions extends Literals with JavaTokenParsers with PackratParsers {

  private lazy val array_index_rule = "@" ~
                                      (namespacedIdentifier <~ "[") ~
                                      (expression <~ "]")
  private lazy val hash_index_rule = "%" ~
                                     (namespacedIdentifier <~ "{") ~
                                     (expression <~ "}")
  
  lazy val expression: PackratParser[AST] = ternaryOp

  // TODO: left        or xor
  // TODO: left        and
  // TODO: right       not
  // TODO: nonassoc    list operators (rightward)
  // TODO: left        , =>
  // TODO: right       = += -= *= etc.

  // right       ?:
  lazy val ternaryOp: PackratParser[AST] = logicalOrOp ~ "?" ~ ternaryOp ~ ":" ~ ternaryOp ^^ {
    case cond ~ "?" ~ trueExpr ~ ":" ~ falseExpr => TernaryOpNode(cond, trueExpr, falseExpr)
  } | logicalOrOp

  // left        ||           TODO: //
  lazy val logicalOrOp: PackratParser[AST] = logicalOrOp ~ """\|\||//""".r ~ logicalAndOp ^^ {
    case left ~ op ~ right => ShortCircuitBinaryOpNode(left, op, right)
  } | logicalAndOp

  // left        &&
  lazy val logicalAndOp: PackratParser[AST] = logicalAndOp ~ "&&" ~ bitOrOp ^^ {
    case left ~ op ~ right => ShortCircuitBinaryOpNode(left, op, right)
  } | bitOrOp

  // left        | ^
  lazy val bitOrOp: PackratParser[AST] = bitOrOp ~ "[|^]".r ~ bitAndOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | bitAndOp

  // left        &
  lazy val bitAndOp: PackratParser[AST] = bitAndOp ~ "&" ~ eqOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | eqOp

  // nonassoc    == != eq ne cmp ~~
  lazy val eqOp: PackratParser[AST] = eqOp ~ "[!=]=|<=>|eq|ne|cmp".r ~ relOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | relOp

  // nonassoc    < > <= >= lt gt le ge
  lazy val relOp: PackratParser[AST] = relOp ~ "[<>]=?|lt|gt|le|ge".r ~ bitShiftOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | bitShiftOp

  // TODO: nonassoc    named unary operators

  // left        << >>
  lazy val bitShiftOp: PackratParser[AST] = bitShiftOp ~ "<<|>>".r ~ addOp            ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | addOp

  // left        + - .
  lazy val addOp: PackratParser[AST] = addOp ~ "[-+.]".r ~ mulOp            ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | mulOp

  // left        * / % x
  lazy val mulOp: PackratParser[AST] = mulOp ~ "[*/%x]".r ~ expOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | expOp

  // TODO: left        =~ !~
  // TODO: right       ! ~ \ and unary + and -

  // This one is right-recursive (associative) instead of left
  // right       **
  lazy val expOp: PackratParser[AST] = applyOp ~ "**" ~ expOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | applyOp

  // TODO: nonassoc    ++ --

  // left        ->
  lazy val applyOp: PackratParser[AST] = (applyOp <~ "->") ~ namespacedIdentifier ~ ("(" ~> repsep(expression, ",") <~ ")").? ^^ {
    case invocant ~ method ~ Some(args) => MethodCallNode(invocant, method, args)
    case invocant ~ method ~ None       => MethodCallNode(invocant, method, List())
  } | subroutineCall

  lazy val subroutineCall: PackratParser[AST] = namespacedIdentifier ~ ("(" ~> repsep(expression, ",") <~ ")") ^^ {
    case sub ~ args => SubroutineCallNode(sub, args)
  } | simpleExpression

  // TODO: left        terms and list operators (leftward)

  lazy val simpleExpression: PackratParser[AST] = (
      arrayIndex
    | hashIndex
    | hash
    | array
    | range
    | literalValue
    | declaration
    | variable
    | expressionParens
    | signedExpressionParens
  )

  def expressionParens: Parser[AST] = "(" ~> expression <~ ")"
  def signedExpressionParens: PackratParser[AST] = "[-+!]".r ~ expressionParens ^^ {
    case "+" ~ expr => expr
    case "-" ~ expr => PrefixUnaryOpNode(expr, "-")
    case "!" ~ expr => PrefixUnaryOpNode(expr, "!")
  }

  // List stuff
  def list: Parser[List[AST]] = (literal(",").? ~> repsep(expression, ",") <~ literal(",").?)
  def array: Parser[ArrayLiteralNode] = "[" ~> list <~ "]" ^^ ArrayLiteralNode

  // Hash stuff
  def barehashKey: Parser[StringLiteralNode] =
    """[0-9\w_]*""".r ^^ StringLiteralNode
  def hashKey: Parser[AST] = scalar | barehashKey
  def pair: Parser[PairLiteralNode] =
    (hashKey <~ "=>") ~ expression ^^ { case k ~ v => PairLiteralNode(k, v) }
  def hashContent: Parser[List[PairLiteralNode]] =
    repsep(pair, ",")
  def hash: Parser[HashLiteralNode] =
    "{" ~> hashContent <~ "}" ^^ HashLiteralNode

  // range stuff
  def rangeOperands: Parser[AST] = (
      literalValue
    | variable
  )

  def range: Parser[RangeLiteralNode] = rangeOperands ~ ".." ~ rangeOperands ^^ {
    case s ~ _ ~ e => RangeLiteralNode(s, e)
  }

  // Variable stuff
  def sigil = """[$@%]""".r
  def varname = sigil ~ namespacedIdentifier ^^ { case a ~ b => a + b }
  def variable = varname ^^ VariableAccessNode

  def simpleScalar = "$" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("$" + i) }
  def simpleArray  = "@" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("@" + i) }
  def simpleHash   = "%" ~> namespacedIdentifier ^^ {i: String => VariableAccessNode("%" + i) }

  def declaration = "my" ~> varname ~ ("=" ~> expression).? ^^ {
    case v ~ expr => VariableDeclarationNode(v, expr.getOrElse(UndefLiteralNode()))
  }

  def arrayIndex = array_index_rule ^^ {
    case "@" ~ i ~ expr => ArrayElementAccessNode("@" + i, expr)
  }

  def hashIndex = hash_index_rule ^^ {
    case "%" ~ i ~ expr => HashElementAccessNode("%" + i, expr)
  }

  def scalar: Parser[AST] = simpleScalar | arrayIndex | hashIndex | literalValue



}
