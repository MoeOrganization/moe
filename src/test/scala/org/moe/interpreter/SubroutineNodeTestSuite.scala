package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class SubroutineNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with one-arg sub") {
    // sub plus_ten($n) { $n + 10 } plus_ten(plus_ten(10)) }
    val ast = wrapSimpleAST(
      List(
        SubroutineDeclarationNode(
          "plus_ten",
          List("$n"),
          StatementsNode(
            List(
              MethodCallNode(
                VariableAccessNode("$n"),
                "+",
                List(IntLiteralNode(10))
              )
            )
          )
        ),
        SubroutineCallNode(
          "plus_ten",
          List(
            SubroutineCallNode(
              "plus_ten",
              List(
                IntLiteralNode(10)
              )
            )
          )
        )
      )
    )
    val result = runSimpleAST(ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 30)
  }

  test("... basic test with closure") {
    // my $foo = 0; sub add_foo { $foo++ }
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
    val result = runSimpleAST(ast)
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
      runSimpleAST(ast)
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
                    IncrementNode(VariableAccessNode("$n"), true)
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
    val result = runSimpleAST(ast)
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
    val result = runSimpleAST(ast)
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
                    IncrementNode(VariableAccessNode("$a"), true)
                  )
                )
              ),
              IncrementNode(VariableAccessNode("$a"), true)
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
    val result = runSimpleAST(ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 2)
  }

  test("... basic test against sub foo { sub bar {} }") {
    val ast = wrapSimpleAST(
      List(
        SubroutineDeclarationNode(
          "foo",
          List(),
          StatementsNode(
            List(
              SubroutineDeclarationNode(
                "bar",
                List(),
                StatementsNode(List())
              )
            )
          )
        )
      )
    )
    intercept[MoeErrors.NotAllowed] {
      runSimpleAST(ast)
    }
  }
}
