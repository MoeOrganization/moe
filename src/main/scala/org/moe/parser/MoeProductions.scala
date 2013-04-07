package org.moe.parser

import scala.util.parsing.combinator._
import org.moe.ast._

trait MoeProductions extends MoeLiterals with JavaTokenParsers with PackratParsers {

  /**
   *********************************************************************
   * This part of the parser deals mostly with 
   * Ternary, Unary and Binary operations and
   * implements the precedance ordering.
   *********************************************************************
   */
  
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

  // left        + - ~
  lazy val addOp: PackratParser[AST] = addOp ~ "[-+~]".r ~ mulOp            ^^ {
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
  lazy val expOp: PackratParser[AST] = coerceOp ~ "**" ~ expOp ^^ {
    case left ~ op ~ right => BinaryOpNode(left, op, right)
  } | coerceOp

  // Symbolic unary -- left        + (num), ? (bool), ~ (str)
  // used for explicit coercion
  // (see: http://perlcabal.org/syn/S03.html#Symbolic_unary_precedence)

  lazy val coerceOp: PackratParser[AST] = "[+?~]".r ~ fileTestOps ^^ {
    case op ~ expr => PrefixUnaryOpNode(expr, op)
  } | fileTestOps

  // TODO: nonassoc    ++ --

  lazy val fileTestOps: PackratParser[AST] = "-[erwx]".r ~ applyOp ^^ {
    case op ~ expr => PrefixUnaryOpNode(expr, op)
  } | applyOp

  /**
   *********************************************************************
   * Here we have method and subroutine calling
   * both of which are still expressions.
   *********************************************************************
   */

  // left        .
  lazy val applyOp: PackratParser[AST] = (applyOp <~ ".") ~ namespacedIdentifier ~ ("(" ~> repsep(expression, ",") <~ ")").? ^^ {
    case invocant ~ method ~ Some(args) => MethodCallNode(invocant, method, args)
    case invocant ~ method ~ None       => MethodCallNode(invocant, method, List())
  } | subroutineCall

  lazy val subroutineCall: PackratParser[AST] = namespacedIdentifier ~ ("(" ~> repsep(expression, ",") <~ ")") ^^ {
    case sub ~ args => SubroutineCallNode(sub, args)
  } | listOpLeftward

  // left        terms and list operators (leftward)
  lazy val listOpLeftward: PackratParser[AST] = namespacedIdentifier ~ rep1sep(expression, ",") ^^ {
    case sub ~ args => SubroutineCallNode(sub, args)
  } | anonCodeCall

  def anonCodeInvocant: PackratParser[AST] = (
      variable
    | attribute
  )

  lazy val anonCodeCall: PackratParser[AST] = anonCodeInvocant ~ "." ~ ("(" ~> repsep(expression, ",") <~ ")") ^^ {
    case anonCode ~ _ ~ args => MethodCallNode(anonCode, "call", args)
  } | simpleExpression

  // TODO: left        terms and list operators (leftward)

  /**
   *********************************************************************
   * Now build our set of simpleExpressions
   * which are not Unary, Binary or Ternay
   * This includes much of what comes after 
   * it in this file.
   *********************************************************************
   */

  lazy val simpleExpression: PackratParser[AST] = (
      arrayIndex
    | hashIndex
    | hash
    | array
    | pair
    | range
    | code
    | literalValue
    | classAccess
    | variable
    | attribute
    | expressionParens
    | signedExpressionParens
  )

  def expressionParens: Parser[AST] = "(" ~> expression <~ ")"
  def signedExpressionParens: PackratParser[AST] = "[-+!]".r ~ expressionParens ^^ {
    case "+" ~ expr => expr
    case "-" ~ expr => PrefixUnaryOpNode(expr, "-")
    case "!" ~ expr => PrefixUnaryOpNode(expr, "!")
  }

  /**
   *********************************************************************
   * Now we get into a lot of creation of 
   * complex literal values such as Hash,
   * Pair, Array, Code, etc
   *********************************************************************
   */

  // List/Array Literals

  def list: Parser[List[AST]] = (literal(",").? ~> repsep(expression, ",") <~ literal(",").?)
  def array: Parser[ArrayLiteralNode] = "[" ~> list <~ "]" ^^ ArrayLiteralNode

  // Pair Literals

  def pair: Parser[PairLiteralNode] = (hashKey <~ "=>") ~ expression ^^ { 
    case k ~ v => PairLiteralNode(k, v) 
  }

  // Hash Literals
  
  def barehashKey: Parser[StringLiteralNode] = """[0-9\w_]+""".r ^^ StringLiteralNode
  def hashKey: Parser[AST] = variable | arrayIndex | hashIndex | literalValue | barehashKey

  def hashContent: Parser[List[PairLiteralNode]] = repsep(pair, ",")
  def hash: Parser[HashLiteralNode] = "{" ~> hashContent <~ "}" ^^ HashLiteralNode

  // Range Literals

  def rangeOperands: Parser[AST] = (
      floatNumber
    | intNumber
    | octIntNumber
    | hexIntNumber
    | binIntNumber
    | zeroNumber
    | string
    | variable
  )

  def range: Parser[RangeLiteralNode] = rangeOperands ~ ".." ~ rangeOperands ^^ {
    case s ~ _ ~ e => RangeLiteralNode(s, e)
  }

  /**
   *********************************************************************
   * This section begins to deal with variable
   * declaration and access.
   *********************************************************************
   */

  def identifier           = ident
  def namespaceSeparator   = "::"
  def namespacedIdentifier = rep1sep(identifier, namespaceSeparator) ^^ { _.mkString(namespaceSeparator) }

  def sigil = """[$@%&]""".r
  
  // access

  def variableName = sigil ~ namespacedIdentifier ^^ { case a ~ b => a + b }
  def variable     = variableName ^^ VariableAccessNode

  def attributeName = sigil ~ "!" ~ identifier ^^ { case a ~ b ~ c => a + b + c }
  def attribute     = attributeName ^^ AttributeAccessNode

  def classAccess = "^" ~> namespacedIdentifier ^^ ClassAccessNode

  // declaration

  private lazy val array_index_rule = "@" ~ (namespacedIdentifier <~ "[") ~ (expression <~ "]")
  private lazy val hash_index_rule  = "%" ~ (namespacedIdentifier <~ "{") ~ (expression <~ "}")

  def arrayIndex = array_index_rule ^^ {
    case "@" ~ i ~ expr => ArrayElementAccessNode("@" + i, expr)
  }

  def hashIndex = hash_index_rule ^^ {
    case "%" ~ i ~ expr => HashElementAccessNode("%" + i, expr)
  }

  // assignment

  def variableDeclaration = "my" ~> variableName ~ ("=" ~> expression).? <~ statementDelim ^^ {
    case v ~ expr => VariableDeclarationNode(v, expr.getOrElse(UndefLiteralNode()))
  }  

  def variableAssignment = variableName ~ "=" ~ expression <~ statementDelim ^^ {
    case v ~ _ ~ expr => VariableAssignmentNode(v, expr)
  }

  def attributeAssignment = attributeName ~ "=" ~ expression <~ statementDelim ^^ {
    case v ~ _ ~ expr => AttributeAssignmentNode(v, expr)
  }

  def arrayElementAssignment = array_index_rule ~ "=" ~ expression <~ statementDelim ^^ {
    case "@" ~ array ~ index_expr ~ "=" ~ value_expr => ArrayElementLvalueNode("@" + array, index_expr, value_expr)
  }

  /**
   *********************************************************************
   * Now we are getting into statements,
   * however the line is a little blurred 
   * here by the "code" production because
   * it is referenced above in "simpleExpressions"
   *********************************************************************
   */

  def statementDelim: Parser[List[String]] = rep1(";")

  def statements: Parser[StatementsNode] = repsep((
      blockStatement
    | declarationStatement
    | statement
  ), statementDelim.?) ^^ StatementsNode

  def block: Parser[StatementsNode] = "{" ~> statements <~ "}"

  def doBlock: Parser[StatementsNode] = "do".r ~> block
  def scopeBlock: Parser[ScopeNode] = block ^^ { ScopeNode(_) }

  // Parameters

  def parameter = ("[*:]".r).? ~ sigil ~ namespacedIdentifier ~ "?".? ^^ { 
    case None      ~  a  ~ b ~ None      => ParameterNode(a + b)
    case None      ~  a  ~ b ~ Some("?") => ParameterNode(a + b, optional = true)
    case Some(":") ~  a  ~ b ~ None      => ParameterNode(a + b, named = true)
    case Some("*") ~ "@" ~ b ~ None      => ParameterNode("@" + b, slurpy = true)
    case Some("*") ~ "%" ~ b ~ None      => ParameterNode("%" + b, slurpy = true, named = true)
  }

  // Code literals

  def code: Parser[CodeLiteralNode] = ("->" ~> ("(" ~> repsep(parameter, ",") <~ ")").?) ~ block ^^ { 
    case Some(p) ~ b => CodeLiteralNode(SignatureNode(p), b) 
    case None    ~ b => CodeLiteralNode(SignatureNode(List(ParameterNode("@_", slurpy = true))), b) 
  }  

  // Packages

  def packageDecl = ("package" ~> namespacedIdentifier) ~ block ^^ {
    case p ~ b => PackageDeclarationNode(p, b)
  }

  def subroutineDecl: Parser[SubroutineDeclarationNode] = ("sub" ~> namespacedIdentifier ~ ("(" ~> repsep(parameter, ",") <~ ")").?) ~ rep("is" ~> identifier).? ~ block ^^ { 
    case n ~ Some(p) ~ t ~ b => SubroutineDeclarationNode(n, SignatureNode(p), b, t) 
    case n ~ None    ~ t ~ b => SubroutineDeclarationNode(n, SignatureNode(List()), b, t) 
  }

  // Classes

  def attributeDecl = "has" ~> attributeName ~ ("=" ~> expression).? <~ statementDelim ^^ {
    case v ~ expr => AttributeDeclarationNode(v, expr.getOrElse(UndefLiteralNode()))
  }

  def methodDecl: Parser[MethodDeclarationNode] = ("method" ~> namespacedIdentifier ~ ("(" ~> repsep(parameter, ",") <~ ")").?) ~ block ^^ { 
    case n ~ Some(p) ~ b => MethodDeclarationNode(n, SignatureNode(p), b) 
    case n ~ None    ~ b => MethodDeclarationNode(n, SignatureNode(List()), b) 
  }

  def constructorDecl: Parser[ConstructorDeclarationNode] = ("BUILD" ~> ("(" ~> repsep(parameter, ",") <~ ")").?) ~ block ^^ { 
    case Some(p) ~ b => ConstructorDeclarationNode(SignatureNode(p), b) 
    case None    ~ b => ConstructorDeclarationNode(SignatureNode(List()), b) 
  }

  def destructorDecl: Parser[DestructorDeclarationNode] = ("DEMOLISH" ~> ("(" ~> repsep(parameter, ",") <~ ")").?) ~ block ^^ { 
    case Some(p) ~ b => DestructorDeclarationNode(SignatureNode(p), b) 
    case None    ~ b => DestructorDeclarationNode(SignatureNode(List()), b) 
  }

  def classBodyParts: Parser[AST] = (
      methodDecl
    | constructorDecl
    | destructorDecl
    | attributeDecl
  )

  def classBodyContent    : Parser[StatementsNode] = rep(classBodyParts) ^^ StatementsNode
  def classBody           : Parser[StatementsNode] = "{" ~> classBodyContent <~ "}"

  def classDecl = ("class" ~> namespacedIdentifier) ~ ("extends" ~> namespacedIdentifier).? ~ classBody ^^ {
    case c ~ s ~ b => ClassDeclarationNode(c, s, b) 
  }

  /**
   *********************************************************************
   * From here on out it is mostly about 
   * control statements.
   *********************************************************************
   */

  def useStatement: Parser[UseStatement] = ("use" ~> namespacedIdentifier) <~ statementDelim ^^ UseStatement

  def elseBlock: Parser[IfStruct] = "else" ~> block ^^ { 
    case body => new IfStruct(BooleanLiteralNode(true), body) 
  }

  def elsifBlock: Parser[IfStruct] = "elsif" ~> ("(" ~> expression <~ ")") ~ block ~ (elsifBlock | elseBlock).? ^^ {
    case cond ~ body ~ None => new IfStruct(cond, body)
    case cond ~ body ~ more => new IfStruct(cond, body, more)
  }

  def ifElseBlock: Parser[AST] = "if" ~> ("(" ~> expression <~ ")") ~ block ~ (elsifBlock | elseBlock).? ^^ { 
    case if_cond ~ if_body ~ None        => IfNode(new IfStruct(if_cond,if_body)) 
    case if_cond ~ if_body ~ Some(_else) => IfNode(new IfStruct(if_cond,if_body, Some(_else))) 
  }

  def whileBlock: Parser[WhileNode] = "while" ~> ("(" ~> expression <~ ")") ~ block ^^ {
    case cond ~ body => WhileNode(cond, body)
  }

  def untilBlock: Parser[WhileNode] = "until" ~> ("(" ~> expression <~ ")") ~ block ^^ {
    case cond ~ body => WhileNode(PrefixUnaryOpNode(cond, "!"), body)
  }

  // def forLoop = "for" ~ "(" ~> expression <~ ";" ~> expression <~ ";" ~> expression <~ ")" ~ block
  // def foreachLoop = "for(each)?".r ~ varDeclare ~ "(" ~> expression <~ ")" ~ block

  def tryBlock: Parser[TryNode] = ("try" ~> block) ~ rep(catchBlock) ~ rep(finallyBlock) ^^ {
    case a ~ b ~ c => TryNode(a, b, c)
  }

  def catchBlock: Parser[CatchNode] = ("catch" ~ "(") ~> namespacedIdentifier ~ variableName ~ (")" ~> block) ^^ {
    case a ~ b ~ c => CatchNode(a, b, c)
  }

  def finallyBlock: Parser[FinallyNode] = "finally" ~> block ^^ FinallyNode

  /**
   *********************************************************************
   * Lastly, wrap it up with a general "statements"
   * production that encompasess much of the above
   *********************************************************************
   */  

  lazy val blockStatement: Parser[AST] = (
      ifElseBlock
    | whileBlock
    | untilBlock
    | doBlock
    | tryBlock
  )

  lazy val declarationStatement: Parser[AST] = (
      packageDecl
    | subroutineDecl
    | classDecl
  )

  lazy val statement: Parser[AST] = (
      variableDeclaration
    | variableAssignment
    | attributeAssignment  
    | useStatement
    | arrayElementAssignment
    | expression <~ statementDelim.?
    | scopeBlock  
  )

}
