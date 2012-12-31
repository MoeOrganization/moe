package org.moe.runtime

import org.moe.Moe.Errors

class MoeMethod ( 
        private val name : String 
    ) {

    def getName (): String = name 

    def call ( reciever : MoeObject, args : List[MoeObject] ): MoeObject = {
        throw new Errors.UndefinedMethod( name )
    }
}