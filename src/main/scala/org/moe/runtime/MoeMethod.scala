package org.moe.runtime

import org.moe.Moe.Errors

class MoeMethod ( 
        private val name : String,
        private val body : ( MoeObject, List[MoeObject] ) => MoeObject
    ) {

    def getName (): String = name 
    def getBody (): ( MoeObject, List[MoeObject] ) => MoeObject = body

    def execute ( invocant : MoeObject, args : List[MoeObject] ): MoeObject = {
        body( invocant, args )
    }
}