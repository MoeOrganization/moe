package org.moe.runtime

import scala.collection.mutable.HashMap

import org.moe.Moe.Errors

class Environment {

    object Markers {
        val Package  = "__PACKAGE__"
        val Class    = "__CLASS__"
        val Invocant = "__SELF__"        
    }

    private val pad = new HashMap[String,MoeObject]()
    
    private var parent : Environment = _

    def this ( p : Environment ) {
        this()
        parent = p
    }

    def getCurrentPackage  (): MoeObject = getLocal( Markers.Package  )
    def getCurrentClass    (): MoeObject = getLocal( Markers.Class    )
    def getCurrentInvocant (): MoeObject = getLocal( Markers.Invocant )

    def setCurrentPackage  ( p : MoeObject ): Unit = pad.put( Markers.Package,  p )
    def setCurrentClass    ( c : MoeObject ): Unit = pad.put( Markers.Class,    c )
    def setCurrentInvocant ( i : MoeObject ): Unit = pad.put( Markers.Invocant, i )

    def getParent (): Environment = parent
    def isRoot    (): Boolean     = parent == null

    def get ( name : String ): MoeObject = {
        if ( pad.contains( name ) ) return pad( name )
        if ( !isRoot              ) return parent.get( name )
        throw new Errors.ValueNotFound( name )
    }

    def has ( name : String ): Boolean = {
        if ( pad.contains( name ) ) return true
        if ( !isRoot              ) return parent.has( name )
        false
    }

    def create ( name : String, value : MoeObject ): Unit = pad.put( name, value )

    def set ( name : String, value : MoeObject ): Unit = {
        if ( !has( name ) ) throw new Errors.UndefinedValue( name )

        if ( pad.contains( name ) ) {
            pad.put( name, value )
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
            throw new Errors.UndefinedValue( name )
        }
    }

    private def getLocal ( name : String ): MoeObject = pad( name )
    private def hasLocal ( name : String ): Boolean   = pad.contains( name )
    private def setLocal ( name : String, value : MoeObject ): Unit = pad.put( name, value )

}


