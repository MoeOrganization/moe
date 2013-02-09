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

  test("... basic test with constructor") {
    // class Point { } Point->new
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Point",
          None,
          StatementsNode(
            List()
          )
        ),
        MethodCallNode(
          ClassAccessNode("Point"),
          "new",
          List()
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    result.getAssociatedClass match {
      case Some(klass) => klass.getName should equal ("Point")
      case None => assert(false)
    }
  }

  test("... basic test with class and superclass") {
    // class Point { }
    // class Point3D extends Point { }
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

  test("... basic test with class and attributes") {
    // class Point { has $.x = 0; has $.y = 0 }
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Point",
          None,
          StatementsNode(
            List(
              AttributeDeclarationNode(
                "x", //"$.x",
                IntLiteralNode(0)
              ),
              AttributeDeclarationNode(
                "y", //"$.y",
                IntLiteralNode(0)
              )
            )
          )
        )
      )
    )
    interpreter.eval(runtime, runtime.getRootEnv, ast)

    val point_class = runtime.getRootPackage.getClass("Point").getOrElse(
      throw new Exception("Class expected") // This has been tested
    )

    point_class should haveAttribute("x")
    point_class.getAttribute("x") match {
      case Some(attr) =>
        attr.getDefault match {
          case Some(attr) => attr.asInstanceOf[MoeIntObject].getNativeValue should equal (0)
          case None => assert(false)
        }
      case None => assert(false)
    }
  }

  test("... basic test with class and methods") {
    // class Counter { has $.n; method inc { ++$.n } }
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Counter",
          None,
          StatementsNode(
            List(
              AttributeDeclarationNode(
                "n", //"$.n",
                IntLiteralNode(0)
              ),
              MethodDeclarationNode(
                "inc",
                List(), // FIXME test with params when we have more operators :P
                StatementsNode(
                  List(
                    IncrementNode(AttributeAccessNode("n"))
                  )
                )
              )
            )
          )
        )
      )
    )
    interpreter.eval(runtime, runtime.getRootEnv, ast)

    val counter_class = runtime.getRootPackage.getClass("Counter").getOrElse(
      throw new Exception("Class expected") // This has been tested
    )

    counter_class should haveMethod("inc")
  }

  test("... basic test with method call") {
    // class Foo { method zzz { 42 } } Foo->new->zzz()
    val ast = wrapSimpleAST(
      List(
        ClassDeclarationNode(
          "Foo",
          None,
          StatementsNode(
            List(
              MethodDeclarationNode(
                "zzz",
                List(),
                StatementsNode(
                  List(
                    IntLiteralNode(42)
                  )
                )
              )
            )
          )
        ),
        MethodCallNode(
          MethodCallNode(
            ClassAccessNode("Foo"),
            "new",
            List()
          ),
          "zzz",
          List()
        )
      )
    )
    val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
    result.asInstanceOf[MoeIntObject].getNativeValue should equal (42)
  }
}
