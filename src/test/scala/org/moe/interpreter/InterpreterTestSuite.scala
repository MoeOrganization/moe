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

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
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

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeIntObject ].getNativeValue() === 10 )
    }  

    test("... basic test with Float") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( FloatLiteralNode( 10.5 ) )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeFloatObject ].getNativeValue() === 10.5 )
    } 

    test("... basic test with String") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( StringLiteralNode( "HELLO" ) )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeStringObject ].getNativeValue() === "HELLO" )
    }     

    // some simple logical operators

    test("... basic test with And") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( 
                        AndNode(
                            BooleanLiteralNode( true ),
                            BooleanLiteralNode( false )
                        )
                    )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeBooleanObject ].getNativeValue() === false )
    } 

    test("... basic test with nested And") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( 
                        AndNode(
                            BooleanLiteralNode( true ),
                            AndNode(
                                BooleanLiteralNode( true ),
                                IntLiteralNode( 100 )
                            )
                        )
                    )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeIntObject ].getNativeValue() === 100 )
    }    

    test("... basic test with Or") {
        val ast = CompilationUnitNode(
            ScopeNode(
                StatementsNode(
                    List( 
                        OrNode(
                            BooleanLiteralNode( true ),
                            BooleanLiteralNode( false )
                        )
                    )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeBooleanObject ].getNativeValue() === true )
    } 

}