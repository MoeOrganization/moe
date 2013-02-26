package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class WhileNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with while loop") {
    // my ($foo, $bar) = (0, 0);
    // while ($foo < 10) { $foo++; $bar-- } $bar
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(0)
        ),
        VariableDeclarationNode(
          "$bar",
          IntLiteralNode(0)
        ),
        WhileNode(
          BinaryOpNode(
            VariableAccessNode("$foo"),
            "<",
            IntLiteralNode(10)
          ),
          StatementsNode(
            List(
              PostfixUnaryOpNode(VariableAccessNode("$foo"), "++"),
              PostfixUnaryOpNode(VariableAccessNode("$bar"), "--")
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
