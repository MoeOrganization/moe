package org.moe.interpreter

import org.scalatest.FunSuite

import org.moe.runtime._
import org.moe.ast._

class PackageNodeTestSuite extends FunSuite with InterpreterTestUtils {
  test("... basic test with package and single sub") {
    // package Foo { my $foo = 0; sub add_foo { $foo++ }
    // add_foo(); add_foo(); add_foo(); $foo }
    val ast = wrapSimpleAST(
      List(
        PackageDeclarationNode(
          "Foo",
          StatementsNode(
            List(
              SubroutineDeclarationNode(
                "my_sub",
                SignatureNode(List()),
                StatementsNode(List())
              )
            )
          )
        )
      )
    )
    interpreter.eval(runtime, runtime.getRootEnv, ast)
    assert(runtime.getRootPackage.hasSubPackage("Foo"), "core package has package Foo")
    assert(runtime.getRootPackage.getSubPackage("Foo").exists(_.hasSubroutine("my_sub")), "Foo has sub my_sub")
  }

}
