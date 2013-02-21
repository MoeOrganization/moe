package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class ForNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with foreach loop") {
    // my $bar = 0; for my $foo ('fee', 'fi', 'fo', 'fum') { $bar++ } $bar
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$bar",
          IntLiteralNode(0)
        ),
        ForeachNode(
          VariableDeclarationNode(
            "$foo",
            UndefLiteralNode()
          ),
          ArrayLiteralNode(
            List(
              StringLiteralNode("fee"),
              StringLiteralNode("fi"),
              StringLiteralNode("fo"),
              StringLiteralNode("fum")
            )
          ),
          StatementsNode(
            List(
              // TODO need to test the in-scope
              // topic assignment somehow - maybe
              // when we have more features, like
              // pushing to a result array
              IncrementNode(VariableAccessNode("$bar"))
            )
          )
        ),
        VariableAccessNode("$bar")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === 4)
  }

  test("... basic test with C-style for loop") {
    // my $bar = 0; for (my $foo = 0; $foo < 10; $foo++) { $bar-- } $bar
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$bar",
          IntLiteralNode(0)
        ),
        ForNode(
          VariableDeclarationNode(
            "$foo",
            IntLiteralNode(0)
          ),
          LessThanNode(
            VariableAccessNode("$foo"),
            IntLiteralNode(10)
          ),
          IncrementNode(VariableAccessNode("$foo")),
          StatementsNode(
            List(
              DecrementNode(VariableAccessNode("$bar"))
            )
          )
        ),
        VariableAccessNode("$bar")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === -10)
  }


}
