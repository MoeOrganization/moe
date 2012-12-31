package org.moe 

object Moe {

    val VERSION   = 0.01;
    val AUTHORITY = "cpan:STEVAN";

    object Errors {
        class ValueNotFound      ( msg : String ) extends Exception( msg )
        class UninitializedValue ( msg : String ) extends Exception( msg )
    }

    def main ( args: Array[String] ): Unit = {
        println( "Hello World" )
    }    

}