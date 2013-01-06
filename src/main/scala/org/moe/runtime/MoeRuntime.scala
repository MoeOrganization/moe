package org.moe.runtime

object Runtime {

    object Errors {
        class ValueNotFound         ( msg : String ) extends Exception( msg )
        class InstanceValueNotFound ( msg : String ) extends ValueNotFound( msg )
        class MethodNotFound        ( msg : String ) extends ValueNotFound( msg )
        class AttributeNotFound     ( msg : String ) extends ValueNotFound( msg )
        class SubroutineNotFound    ( msg : String ) extends ValueNotFound( msg )
        class VariableNotFound      ( msg : String ) extends ValueNotFound( msg )

        class UndefinedValue        ( msg : String ) extends Exception( msg )
        class UndefinedMethod       ( msg : String ) extends UndefinedValue( msg )
        class UndefinedSubroutine   ( msg : String ) extends UndefinedValue( msg )

        class MissingValue          ( msg : String ) extends Exception( msg )
        class MissingClass          ( msg : String ) extends MissingValue( msg )
    }

}