package org.moe.runtime

import scala.collection.mutable.HashMap

class MoePackage ( private val name : String ) {

    private val subs = new HashMap[ String, MoeSubroutine ]()

    // Subroutines ...

    def addSubroutine ( sub : MoeSubroutine ): Unit = {
        subs += ( sub.getName() -> sub )
    }

    def getSubroutine ( name : String ): MoeSubroutine = {
        if ( hasSubroutine( name ) ) return subs( name )
        throw new Runtime.Errors.SubroutineNotFound( name )
    }

    def hasSubroutine ( name : String ): Boolean = {
        if ( subs.contains( name ) ) return true
        false
    }

}