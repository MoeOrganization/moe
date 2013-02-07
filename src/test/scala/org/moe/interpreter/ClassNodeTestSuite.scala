package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.ast._

import ClassMatchers._

class ClassNodeTestSuite
  extends FunSuite
  with InterpreterTestUtils
  with ShouldMatchers
  with ClassMatchers {
  test("... basic test with class") {
    // class Point { }
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Point",
          None,
          StatementsNode(
            List()
          )
        )
      )
    )
    interpreter.eval(runtime, runtime.getRootEnv, ast)
    val root_pkg = runtime.getRootPackage
    root_pkg should haveClass("Point")
  }

  test("... basic test with class and superclass") {
    // class Point { }
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Point",
          None,
          StatementsNode(
            List()
          )
        ),
        ClassDeclarationNode(
          "Point3D",
          Some("Point"),
          StatementsNode(
            List()
          )
        )
      )
    )
    interpreter.eval(runtime, runtime.getRootEnv, ast)
    val root_pkg = runtime.getRootPackage
    root_pkg should haveClass("Point")
    root_pkg should haveClass("Point3D")
    val point3d_class = root_pkg.getClass("Point3D")
    point3d_class match {
      case Some(point3d_class) => point3d_class should extendClass("Point")
      case None => assert(false)
    }
  }
}
