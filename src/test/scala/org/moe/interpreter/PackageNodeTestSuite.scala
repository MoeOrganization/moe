package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class PackageNodeTestSuite extends FunSuite with BeforeAndAfter with InterpreterTestUtils {

  test("... basic test with package and closure") {
    // package Foo { my $foo = 0; sub add_foo { $foo++ }
    // add_foo(); add_foo(); add_foo(); $foo }
    val ast = wrapSimpleAST(
      List(
        PackageDeclarationNode(
          "Foo",
          StatementsNode(
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
              SubroutineCallNode("add_foo", List()),
              SubroutineCallNode("add_foo", List()),
              SubroutineCallNode("add_foo", List()),
              VariableAccessNode("$foo")
            )
          )
        )
      )
    )
    val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
    assert(result.asInstanceOf[MoeIntObject].getNativeValue === 3)
  }

}
