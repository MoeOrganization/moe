package org.moe.interpreter

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.ast._

class InterpreterTestSuite extends FunSuite with BeforeAndAfter {

    private def basicAST ( nodes : List[ AST ] ) = CompilationUnitNode( ScopeNode( StatementsNode( nodes ) ) )

    test("... basic test with null") {
        val ast = basicAST( List( UndefLiteralNode() ) )
        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result === Runtime.NativeObjects.getUndef() )
    }    

    test("... basic test with Int") {
        val ast = basicAST( List( IntLiteralNode( 10 ) ) )
        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeIntObject ].getNativeValue() === 10 )
    }  

    test("... basic test with Float") {
        val ast = basicAST( List( FloatLiteralNode( 10.5 ) ) )
        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeFloatObject ].getNativeValue() === 10.5 )
    } 

    test("... basic test with String") {
        val ast = basicAST( List( StringLiteralNode( "HELLO" ) ) )
        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeStringObject ].getNativeValue() === "HELLO" )
    }     

    // some simple logical operators

    test("... basic test with And") {
        val ast = basicAST(
            List( 
                AndNode(
                    BooleanLiteralNode( true ),
                    BooleanLiteralNode( false )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeBooleanObject ].getNativeValue() === false )
    } 

    test("... basic test with nested And") {
        val ast = basicAST(
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

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeIntObject ].getNativeValue() === 100 )
    }    

    test("... basic test with Or") {
        val ast = basicAST(
            List( 
                OrNode(
                    BooleanLiteralNode( true ),
                    BooleanLiteralNode( false )
                )
            )
        )

        val result = Interpreter.eval( Runtime.getRootEnv(), ast )
        assert( result.asInstanceOf[ MoeBooleanObject ].getNativeValue() === true )
    } 

}