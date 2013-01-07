package org.moe.interpreter

import scala.collection.mutable.HashMap

import org.moe.runtime._

class Environment {

    private val pad = new HashMap[ String, MoeObject ]()
    
    private var parent : Environment = _

    def this ( p : Environment ) {
        this()
        parent = p
    }

    def getParent (): Environment = parent
    def isRoot    (): Boolean     = parent == null

    def get ( name : String ): MoeObject = {
        if ( hasLocal( name ) ) return getLocal( name )
        if ( !isRoot          ) return parent.get( name )
        throw new Runtime.Errors.ValueNotFound( name )
    }

    def has ( name : String ): Boolean = {
        if ( hasLocal( name ) ) return true
        if ( !isRoot          ) return parent.has( name )
        false
    }

    def create ( name : String, value : MoeObject ): Unit = setLocal( name, value )

    def set ( name : String, value : MoeObject ): Unit = {
        if ( !has( name ) ) throw new Runtime.Errors.UndefinedValue( name )

        if ( hasLocal( name ) ) {
            setLocal( name, value )
        } else {
            var current : Environment = parent
            while ( current != null ) {
                if ( current.hasLocal( name ) ) {
                    current.setLocal( name, value )
                    return;
                }
                else {
                    current = current.getParent
                }
            }
            throw new Runtime.Errors.UndefinedValue( name )
        }
    }

    private def getLocal ( name : String ): MoeObject = pad( name )
    private def hasLocal ( name : String ): Boolean   = pad.contains( name )
    private def setLocal ( name : String, value : MoeObject ): Unit = pad += ( name -> value )

}


