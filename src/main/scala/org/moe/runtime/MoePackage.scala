package org.moe.runtime

import scala.collection.mutable.HashMap

class MoePackage ( private val name : String ) {

    private val vars = new HashMap[ String, MoeVariable   ]()
    private val subs = new HashMap[ String, MoeSubroutine ]()

    def getName (): String = name

    // Variables ...

    def addVariable ( variable : MoeVariable ): Unit = {
        vars += ( variable.getName() -> variable )
    }

    def getVariable ( name : String ): MoeVariable = {
        if ( hasVariable( name ) ) return vars( name )
        throw new Runtime.Errors.VariableNotFound( name )
    }

    def hasVariable ( name : String ): Boolean = {
        if ( vars.contains( name ) ) return true
        false
    }

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