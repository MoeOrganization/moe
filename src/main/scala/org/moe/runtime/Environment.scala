package org.moe.runtime

import scala.collection.mutable.HashMap

import org.moe.Moe.Errors

class Environment {

    object Markers {
        val Package  = "__PACKAGE__";
        val Class    = "__CLASS__";
        val Invocant = "__SELF__";        
    }

    val pad = new HashMap[String,Container]();

    var parent : Environment = _;

    def this ( p : Environment ) {
        this()
        parent = p
    }

    def getCurrentPackage  (): Container = getLocal( Markers.Package  )
    def getCurrentClass    (): Container = getLocal( Markers.Class    )
    def getCurrentInvocant (): Container = getLocal( Markers.Invocant )

    def setCurrentPackage  ( p : Container ): Unit = pad.put( Markers.Package,  p )
    def setCurrentClass    ( c : Container ): Unit = pad.put( Markers.Class,    c )
    def setCurrentInvocant ( i : Container ): Unit = pad.put( Markers.Invocant, i )

    def getParent (): Environment = parent
    def isRoot    (): Boolean     = parent == null

    def get ( name : String ): Container = {
        if ( pad.contains( name ) ) return pad( name )
        if ( !isRoot              ) return parent.get( name )
        throw new Errors.ValueNotFound( name );
    }

    def has ( name : String ): Boolean = {
        if ( pad.contains( name ) ) return true
        if ( !isRoot              ) return parent.has( name )
        false;
    }

    def create ( name : String, value : Container ): Unit = pad.put( name, value )

    def set ( name : String, value : Container ): Unit = {
        if ( !has( name ) ) throw new Errors.UninitializedValue( name )

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
            throw new Errors.UninitializedValue( name )
        }
    }

    private def getLocal ( name : String ): Container = pad( name )
    private def hasLocal ( name : String ): Boolean   = pad.contains( name )
    private def setLocal ( name : String, value : Container ): Unit = pad.put( name, value )

}


