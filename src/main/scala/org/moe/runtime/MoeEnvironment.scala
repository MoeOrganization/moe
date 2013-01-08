package org.moe.runtime

import scala.collection.mutable.HashMap

import org.moe.runtime._

class MoeEnvironment {

    object Markers {
        val Package  = "__PACKAGE__"
        val Class    = "__CLASS__"
        val Invocant = "__SELF__"        
    }

    private val pad = new HashMap[ String, MoeObject ]()
    
    private var parent : MoeEnvironment = _

    def this ( p : MoeEnvironment ) {
        this()
        parent = p
    }

    def getCurrentPackage  (): MoePackage = getLocal( Markers.Package  ).asInstanceOf[ MoePackage ]
    def getCurrentClass    (): MoeClass   = getLocal( Markers.Class    ).asInstanceOf[ MoeClass ]
    def getCurrentInvocant (): MoeObject  = getLocal( Markers.Invocant )

    def setCurrentPackage  ( p : MoeObject ): Unit = setLocal( Markers.Package,  p )
    def setCurrentClass    ( c : MoeObject ): Unit = setLocal( Markers.Class,    c )
    def setCurrentInvocant ( i : MoeObject ): Unit = setLocal( Markers.Invocant, i )


    def getParent (): MoeEnvironment = parent
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
            var current : MoeEnvironment = parent
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


