package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class DoWhileNodeTestSuite extends FunSuite with InterpreterTestUtils {

  test("... basic test with dowhile loop") {
    // my ($foo, $bar) = (10, 0);
    // do { $foo++; $bar-- } while($foo < 10); $bar
    val ast = wrapSimpleAST(
      List(
        VariableDeclarationNode(
          "$foo",
          IntLiteralNode(10)
        ),
        VariableDeclarationNode(
          "$bar",
          IntLiteralNode(0)
        ),
        DoWhileNode(
          LessThanNode(
            VariableAccessNode("$foo"),
            IntLiteralNode(10)
          ),
          StatementsNode(
            List(
              IncrementNode(VariableAccessNode("$foo")),
              DecrementNode(VariableAccessNode("$bar"))
            )
          )
        ),
        VariableAccessNode("$bar")
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(result.unboxToInt.get === -1)
  }

}
