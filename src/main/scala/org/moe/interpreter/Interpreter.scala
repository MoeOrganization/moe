package org.moe.interpreter

import org.moe.ast._

object Interpreter {

    def eval ( env : Environment, node : AST ): Unit = {
        node match {
            // containers

            case CompilationUnitNode ( body  ) => eval( env, body )
            case StatementsNode      ( nodes ) => nodes.foreach( (n) => eval( env, n ) )
            case ScopeNode           ( body  ) => eval( new Environment( env ), body )

            // literals

            case LiteralNode ( value ) => { }

            case SelfLiteralNode  () => { }
            case ClassLiteralNode () => { }
            case SuperLiteralNode () => { }

            case PairLiteralNode ( key, value ) => { }

            case ArrayLiteralNode ( values ) => { }
            case HashLiteralNode  ( map    ) => { }

            // unary operators

            case IncrementNode ( reciever ) => { }
            case DecrementNode ( reciever ) => { }
            case NotNode       ( reciever ) => { }

            // binary operators

            case AndNode ( lhs, rhs ) => { }
            case OrNode  ( lhs, rhs ) => { }

            // value lookup, assignment and declaration

            case ClassAccessNode      ( name ) => { }
            case ClassDeclarationNode ( name, superclass, body ) => { }

            case PackageDeclarationNode ( name, body ) => { }

            case ConstructorDeclarationNode ( params, body ) => { }
            case DestructorDeclarationNode  ( params, body ) => { }

            case MethodDeclarationNode     ( name, params, body ) => { }
            case SubroutineDeclarationNode ( name, params, body ) => { }

            case AttributeAccessNode      ( name             ) => { }
            case AttributeAssignmentNode  ( name, expression ) => { }
            case AttributeDeclarationNode ( name, expression ) => { }

            case VariableAccessNode      ( name             ) => { }
            case VariableAssignmentNode  ( name, expression ) => { }
            case VariableDeclarationNode ( name, expression ) => { }

            // operations

            case MethodCallNode     ( invocant, method_name, args ) => { }
            case SubroutineCallNode ( function_name, args ) => { }

            // statements

            case IfNode          ( if_condition, if_body ) => { }
            case IfElseNode      ( if_condition, if_body, else_body ) => { }
            case IfElsifNode     ( if_condition, if_body, elsif_condition, elsif_body ) => { }
            case IfElsifElseNode ( if_condition, if_body, elsif_condition, elsif_body, else_body ) => { }

            case UnlessNode     ( unless_condition, unless_body ) => { }
            case UnlessElseNode ( unless_condition, unless_body, else_body ) => { }

            case TryNode     ( body, catch_nodes, finally_nodes ) => { }
            case CatchNode   ( type_name, local_name, body ) => { }
            case FinallyNode ( body ) => { }

            case WhileNode   ( condition, body ) => { }
            case DoWhileNode ( condition, body ) => { }

            case ForeachNode ( topic, list, body ) => { }
            case ForNode ( init, condition, update, body ) => { }
        }
    }

}