package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class SubroutineNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with one-arg sub") {
    // package Foo { sub inc($n) { ++$n }
    // my $n = 1; inc(inc(n)) }
    val ast = wrapSimpleAST(
      List(
        SubroutineDeclarationNode(
          "inc",
          List("$n"),
          StatementsNode(
            List(
              // TODO just add one when we have addition
              IncrementNode(VariableAccessNode("$n"))
            )
          )
        ),
        VariableDeclarationNode("$n", IntLiteralNode(1)),
        SubroutineCallNode(
          "inc",
          List(
            SubroutineCallNode(
              "inc",
              List(
                VariableAccessNode("$n")
              )
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

  test("... basic test with closure") {
    // package Foo { my $foo = 0; sub add_foo { $foo++ }
    // add_foo(); add_foo(); add_foo(); $foo }
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(0)
        ),
        SubroutineDeclarationNode(
          "add_foo",
          List(),
          StatementsNode(
            List(
              IncrementNode(VariableAccessNode("$foo"))
            )
          )
        ),
        SubroutineCallNode("add_foo", List(VariableAccessNode("$foo"))),
        SubroutineCallNode("add_foo", List(VariableAccessNode("$foo"))),
        SubroutineCallNode("add_foo", List(VariableAccessNode("$foo"))),
        VariableAccessNode("$foo")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

}
