package org.moe.runtime

class MoeSubroutine ( 
        private val name : String,
        private val body : ( List[ MoeObject ] ) => MoeObject
    ) {

    def this ( name : String ) = {
        // yadda yadda yadda
        this( name, ( _, _ ) => throw new Runtime.Errors.UndefinedSubroutine() )
    }

    def getName (): String = name 
    def getBody (): ( List[ MoeObject ] ) => MoeObject = body

    def execute ( args : List[ MoeObject ] ): MoeObject = body( args )
}