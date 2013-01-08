package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

object Interpreter {

    val stub = new MoeObject()

    def eval ( env : MoeEnvironment, node : AST ): MoeObject = {
        node match {
            // containers

            case CompilationUnitNode ( body  ) => eval( env, body )
            case ScopeNode           ( body  ) => eval( new MoeEnvironment( env ), body )
            case StatementsNode      ( nodes ) => {
                var result : MoeObject = Runtime.NativeObjects.getUndef()
                for ( val node <- nodes ) {
                    result = eval( env, node )
                }
                result
            }

            // literals

            case IntLiteralNode     ( value ) => Runtime.NativeObjects.getInt( value )
            case FloatLiteralNode   ( value ) => Runtime.NativeObjects.getFloat( value )
            case StringLiteralNode  ( value ) => Runtime.NativeObjects.getString( value )
            case BooleanLiteralNode ( value ) => value match {
                case true  => Runtime.NativeObjects.getTrue()
                case false => Runtime.NativeObjects.getFalse()
            }

            case UndefLiteralNode () => Runtime.NativeObjects.getUndef()
            case SelfLiteralNode  () => env.getCurrentInvocant()
            case ClassLiteralNode () => env.getCurrentClass()
            case SuperLiteralNode () => {
                val klass = env.getCurrentClass()
                klass.getSuperclass() match {
                    case s:MoeClass => s
                    case _          => throw new Runtime.Errors.SuperclassNotFound( klass.getName() )
                }
            }

            case PairLiteralNode ( key, value ) => stub

            case ArrayLiteralNode ( values ) => stub
            case HashLiteralNode  ( map    ) => stub

            // unary operators

            case IncrementNode ( reciever ) => stub
            case DecrementNode ( reciever ) => stub
            case NotNode       ( reciever ) => stub

            // binary operators

            case AndNode ( lhs, rhs ) => {
                val left_result = eval( env, lhs )
                left_result.isTrue() match {
                    case true  => eval( env, rhs )
                    case false => left_result
                }
            }

            case OrNode ( lhs, rhs ) => {
                val left_result = eval( env, lhs )
                left_result.isTrue() match {
                    case true  => left_result
                    case false => eval( env, rhs )
                }
            }

            // value lookup, assignment and declaration

            case ClassAccessNode      ( name ) => stub
            case ClassDeclarationNode ( name, superclass, body ) => stub

            case PackageDeclarationNode ( name, body ) => stub

            case ConstructorDeclarationNode ( params, body ) => stub
            case DestructorDeclarationNode  ( params, body ) => stub

            case MethodDeclarationNode     ( name, params, body ) => stub
            case SubroutineDeclarationNode ( name, params, body ) => stub

            case AttributeAccessNode      ( name             ) => stub
            case AttributeAssignmentNode  ( name, expression ) => stub
            case AttributeDeclarationNode ( name, expression ) => stub

            case VariableAccessNode      ( name             ) => stub
            case VariableAssignmentNode  ( name, expression ) => stub
            case VariableDeclarationNode ( name, expression ) => stub

            // operations

            case MethodCallNode     ( invocant, method_name, args ) => stub
            case SubroutineCallNode ( function_name, args ) => stub

            // statements

            case IfNode          ( if_condition, if_body ) => stub
            case IfElseNode      ( if_condition, if_body, else_body ) => stub
            case IfElsifNode     ( if_condition, if_body, elsif_condition, elsif_body ) => stub
            case IfElsifElseNode ( if_condition, if_body, elsif_condition, elsif_body, else_body ) => stub

            case UnlessNode     ( unless_condition, unless_body ) => stub
            case UnlessElseNode ( unless_condition, unless_body, else_body ) => stub

            case TryNode     ( body, catch_nodes, finally_nodes ) => stub
            case CatchNode   ( type_name, local_name, body ) => stub
            case FinallyNode ( body ) => stub

            case WhileNode   ( condition, body ) => stub
            case DoWhileNode ( condition, body ) => stub

            case ForeachNode ( topic, list, body ) => stub
            case ForNode ( init, condition, update, body ) => stub
        }
    }

}