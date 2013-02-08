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

  test("... basic test with var declared too late") {
    // sub add_foo { $foo++ }
    // my $foo = 0; add_foo(); $foo
    val ast = wrapSimpleAST(
      List(
        SubroutineDeclarationNode(
          "add_foo",
          List(),
          StatementsNode(
            List(
              IncrementNode(VariableAccessNode("$foo"))
            )
          )
        ),
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(0)
        ),
        SubroutineCallNode("add_foo", List()),
        VariableAccessNode("$foo")
      )
    )
    intercept[MoeErrors.VariableNotFound] {
      interpreter.eval(runtime, runtime.getRootEnv, ast)
    }
  }

  test("... basic test with sub using closure behavior") {
    // { my $n = 0; sub inc { ++$n }
    // { inc(); inc(); inc(); }
    val ast = wrapSimpleAST(
      List(
        ScopeNode(
          StatementsNode(
            List(
              VariableDeclarationNode(
                "$n",
                IntLiteralNode(0)
              ),
              SubroutineDeclarationNode(
                "inc",
                List(),
                StatementsNode(
                  List(
                    IncrementNode(VariableAccessNode("$n"))
                  )
                )
              )
            )
          )
        ),
        ScopeNode(
          StatementsNode(
            List(
              SubroutineCallNode("inc", List()),
              SubroutineCallNode("inc", List()),
              SubroutineCallNode("inc", List())
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

  test("... basic test with sub checking for avoided closure collision") {
    // { my $n = 0; sub inc { ++$n } }
    // { my $n = 0; inc(); $n }
    val ast = wrapSimpleAST(
      List(
        ScopeNode(
          StatementsNode(
            List(
              VariableDeclarationNode(
                "$n",
                IntLiteralNode(0)
              ),
              SubroutineDeclarationNode(
                "inc",
                List(),
                StatementsNode(
                  List(
                    IncrementNode(VariableAccessNode("$n"))
                  )
                )
              )
            )
          )
        ),
        ScopeNode(
          StatementsNode(
            List(
              VariableDeclarationNode(
                "$n",
                IntLiteralNode(0)
              ),
              SubroutineCallNode("inc", List()),
              VariableAccessNode("$n")
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 0)
  }

  test("... basic test checking that param var is used") {
    // { my $a = 0; sub f($a) { ++$a }  ++$a }
    // { my $b = f(f(0)); $b = f(f(0)); $b }
    val ast = wrapSimpleAST(
      List(
        ScopeNode(
          StatementsNode(
            List(
              VariableDeclarationNode(
                "$a",
                IntLiteralNode(0)
              ),
              SubroutineDeclarationNode(
                "f",
                List("$a"),
                StatementsNode(
                  List(
                    IncrementNode(VariableAccessNode("$a"))
                  )
                )
              ),
              IncrementNode(VariableAccessNode("$a"))
            )
          )
        ),
        ScopeNode(
          StatementsNode(
            List(
              VariableDeclarationNode(
                "$b",
                SubroutineCallNode(
                  "f",
                  List(
                    SubroutineCallNode(
                      "f",
                      List(IntLiteralNode(0))
                    )
                  )
                )
              ),
              VariableAssignmentNode(
                "$b",
                SubroutineCallNode(
                  "f",
                  List(
                    SubroutineCallNode(
                      "f",
                      List(IntLiteralNode(0))
                    )
                  )
                )
              ),
              VariableAccessNode("$b")
            )
          )
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2)
  }
}
