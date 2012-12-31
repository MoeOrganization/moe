package org.moe 

object Moe {

    val VERSION   = 0.01;
    val AUTHORITY = "cpan:STEVAN";

    object Errors {
        class ValueNotFound         ( msg : String ) extends Exception( msg )
        class InstanceValueNotFound ( msg : String ) extends ValueNotFound( msg )
        class MethodNotFound        ( msg : String ) extends ValueNotFound( msg )

        class UndefinedValue        ( msg : String ) extends Exception( msg )
        class UndefinedMethod       ( msg : String ) extends UndefinedValue( msg )

        class MissingValue          ( msg : String ) extends Exception( msg )
        class MissingClass          ( msg : String ) extends MissingValue( msg )
    }

    def main ( args: Array[String] ): Unit = {
        println( "Hello World" )
    }    

}