package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class InterpreterTestSuite extends FunSuite with BeforeAndAfter {

  private case class FooNode() extends AST

  private def basicAST(nodes: List[AST]) =
    CompilationUnitNode(ScopeNode(StatementsNode(nodes)))

  test("... basic test with null") {
    val ast = basicAST(List(UndefLiteralNode()))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result === Runtime.NativeObjects.getUndef)
  }

  test("... basic test with Int") {
    val ast = basicAST(List(IntLiteralNode(10)))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 10)
  }

  test("... basic test with Float") {
    val ast = basicAST(List(FloatLiteralNode(10.5)))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeFloatObject].getNativeValue === 10.5)
  }

  test("... basic test with String") {
    val ast = basicAST(List(StringLiteralNode("HELLO")))
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "HELLO")
  }

  test("... basic test that last value is the one returned") {
    val ast = basicAST(
      List(
        StringLiteralNode("HELLO"),
        StringLiteralNode("WORLD")
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeStringObject].getNativeValue === "WORLD")
  }

  test("... basic test with Array") {
    val ast = basicAST(
      List(
        ArrayLiteralNode(
          List(
            IntLiteralNode(10),
            IntLiteralNode(20)
          )
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)

    val array: List[MoeObject] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 2)
    assert(array(0).asInstanceOf[MoeIntObject].getNativeValue === 10)
    assert(array(1).asInstanceOf[MoeIntObject].getNativeValue === 20)
  }

  test("... complex test with Array") {
    val ast = basicAST(
      List(
        ArrayLiteralNode(
          List(
            IntLiteralNode(10),
            ArrayLiteralNode(
              List(
                IntLiteralNode(20)
              )
            )
          )
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)

    val array: List[ MoeObject ] = result.asInstanceOf[MoeArrayObject].getNativeValue

    assert(array.size === 2)
    assert(array(0).asInstanceOf[ MoeIntObject ].getNativeValue === 10)

    val nested = array(1).asInstanceOf[ MoeArrayObject ].getNativeValue

    assert(nested.size === 1)
    assert(nested(0).asInstanceOf[ MoeIntObject ].getNativeValue === 20)
  }

  test("... basic test with Hash") {
    val ast = basicAST(
      List(
        HashLiteralNode(
          List(
            PairLiteralNode(StringLiteralNode("foo"), IntLiteralNode(10)),
            PairLiteralNode(StringLiteralNode("bar"), IntLiteralNode(20))
          )
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)

    val hash: Map[String, MoeObject] = result.asInstanceOf[MoeHashObject].getNativeValue
    assert(hash("foo").asInstanceOf[MoeIntObject].getNativeValue === 10)
    assert(hash("bar").asInstanceOf[MoeIntObject].getNativeValue === 20)
  }

  // some simple logical operators

  test("... basic test with Not") {
    val ast = basicAST(
      List(
        NotNode(BooleanLiteralNode(true))
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[ MoeBooleanObject ].getNativeValue === false)
  }

  test("... basic (false) test with Not") {
    val ast = basicAST(
      List(
        NotNode(BooleanLiteralNode(false))
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with And") {
    val ast = basicAST(
      List(
        AndNode(
          BooleanLiteralNode(true),
          BooleanLiteralNode(false)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === false)
  }

  test("... basic test with nested And") {
    val ast = basicAST(
      List(
        AndNode(
          BooleanLiteralNode(true),
          AndNode(
            BooleanLiteralNode(true),
            IntLiteralNode(100)
          )
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 100)
  }

  test("... basic test with Or") {
    val ast = basicAST(
      List(
        OrNode(
          BooleanLiteralNode(true),
          BooleanLiteralNode(false)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeBooleanObject].getNativeValue === true)
  }

  test("... basic test with If") {
    val ast = basicAST(
      List(
        IfNode(
          BooleanLiteralNode(true),
          IntLiteralNode(1)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 1)
  }

  test("... basic (false) test with If") {
    val ast = basicAST(
      List(
        IfNode(
          BooleanLiteralNode(false),
          IntLiteralNode(1)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result === Runtime.NativeObjects.getUndef)
  }

  test("... basic test with IfElse") {
    val ast = basicAST(
      List(
        IfElseNode(
          BooleanLiteralNode(true),
          IntLiteralNode(2),
          IntLiteralNode(3)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2)
  }

  test("... basic (false) test with IfElse") {
    val ast = basicAST(
      List(
        IfElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(2),
          IntLiteralNode(3)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

  test("... basic (true/true) test with IfElsif (true/true)") {
    val ast = basicAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(true),
          IntLiteralNode(5),
          BooleanLiteralNode(true),
          IntLiteralNode(8)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 5)
  }

  test("... basic (false/true) test with IfElsif") {
    val ast = basicAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(false),
          IntLiteralNode(5),
          BooleanLiteralNode(true),
          IntLiteralNode(8)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 8)
  }

  test("... basic (false/false) test with IfElseIf") {
    val ast = basicAST(
      List(
        IfElsifNode(
          BooleanLiteralNode(false),
          IntLiteralNode(13),
          BooleanLiteralNode(false),
          IntLiteralNode(21)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result === Runtime.NativeObjects.getUndef)
  }

  test("... basic (true/true) test with IfElsifElse") {
    val ast = basicAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(true),
          IntLiteralNode(34),
          BooleanLiteralNode(true),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 34)
  }

  test("... basic (false/true) test with IfElsifElse") {
    val ast = basicAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(34),
          BooleanLiteralNode(true),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 55)
  }

  test("... basic (false/false) test with IfElsifElse") {
    val ast = basicAST(
      List(
        IfElsifElseNode(
          BooleanLiteralNode(false),
          IntLiteralNode(34),
          BooleanLiteralNode(false),
          IntLiteralNode(55),
          IntLiteralNode(89)
        )
      )
    )
    val result = Interpreter.eval(Runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 89)
  }

  test("... basic test with Unless") {
    val ast = basicAST(
      List(
        UnlessNode(
          BooleanLiteralNode( false ),
          IntLiteralNode( 42 )
        )
      )
    )
    val result = Interpreter.eval( Runtime.getRootEnv, ast )
    assert( result.asInstanceOf[ MoeIntObject ].getNativeValue === 42 )
  }

  test("... basic (true) test with Unless") {
    val ast = basicAST(
      List(
        UnlessNode(
          BooleanLiteralNode( true ),
          IntLiteralNode( 42 )
        )
      )
    )
    val result = Interpreter.eval( Runtime.getRootEnv, ast )
    assert( result === Runtime.NativeObjects.getUndef )
  }

  test("... basic test with UnlessElse") {
    val ast = basicAST(
      List(
        UnlessElseNode(
          BooleanLiteralNode( false ),
          IntLiteralNode( 42 ),
          IntLiteralNode( 21 )
        )
      )
    )
    val result = Interpreter.eval( Runtime.getRootEnv, ast )
    assert( result.asInstanceOf[ MoeIntObject ].getNativeValue === 42 )
  }

  test("... basic (true) test with UnlessElse") {
    val ast = basicAST(
      List(
        UnlessElseNode(
          BooleanLiteralNode( true ),
          IntLiteralNode( 42 ),
          IntLiteralNode( 21 )
        )
      )
    )
    val result = Interpreter.eval( Runtime.getRootEnv, ast )
    assert( result.asInstanceOf[ MoeIntObject ].getNativeValue === 21 )
  }

  test("... unknown node") {
    val ast = basicAST(
      List(
        FooNode()
      )
    )
    intercept[Runtime.Errors.UnknownNode] {
      Interpreter.eval(Runtime.getRootEnv, ast)
    }
  }

}
