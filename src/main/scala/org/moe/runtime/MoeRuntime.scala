package org.moe.runtime

object Runtime {

    object Errors {
        class MoeException          ( msg : String ) extends Exception( msg )
        class MoeProblem            ( msg : String ) extends MoeException( msg )

        // mo money is-a mo problem, plain an simple yo
        class MoeMoney              ( msg : String ) extends MoeProblem( msg )

        class NotAllowed            ( msg : String ) extends MoeProblem( msg )
        class MethodNotAllowed      ( msg : String ) extends NotAllowed( msg )

        class ValueNotFound         ( msg : String ) extends MoeProblem( msg )
        class PackageNotFound       ( msg : String ) extends ValueNotFound( msg )
        class InstanceValueNotFound ( msg : String ) extends ValueNotFound( msg )
        class ClassNotFound         ( msg : String ) extends ValueNotFound( msg )
        class MethodNotFound        ( msg : String ) extends ValueNotFound( msg )
        class AttributeNotFound     ( msg : String ) extends ValueNotFound( msg )
        class SubroutineNotFound    ( msg : String ) extends ValueNotFound( msg )
        class VariableNotFound      ( msg : String ) extends ValueNotFound( msg )

        class UndefinedValue        ( msg : String ) extends MoeProblem( msg )
        class UndefinedMethod       ( msg : String ) extends UndefinedValue( msg )
        class UndefinedSubroutine   ( msg : String ) extends UndefinedValue( msg )

        class MissingValue          ( msg : String ) extends MoeProblem( msg )
        class MissingClass          ( msg : String ) extends MissingValue( msg )
    }

}