package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class InterpreterTestSuite extends FunSuite with BeforeAndAfter {

    test("... basic test with null") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( UndefLiteralNode() )
                )
            )
        )

        val result = Interpreter.eval( Runtime.RootEnv, ast )
        assert( result === Runtime.NativeObjects.getUndef() )
    }    

    test("... basic test with Int") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( IntLiteralNode( 10 ) )
                )
            )
        )

        val result = Interpreter.eval( Runtime.RootEnv, ast )
        assert( result.asInstanceOf[ MoeIntObject ].getNativeValue() ===  10 )
    }    

}