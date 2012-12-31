package org.moe.runtime

import scala.collection.mutable.HashMap

import org.moe.Moe.Errors

class MoeClass (
        private val name       : String,
        private val version    : String,
        private val authority  : String
    ) {
    
    private var superclass : MoeClass = _

    private val methods = new HashMap[String,MoeMethod]()

    def getName      (): String = name
    def getVersion   (): String = version
    def getAuthority (): String = authority

    def getSuperclass (): MoeClass = superclass
    def hasSuperclass (): Boolean  = superclass != null
    def setSuperclass ( s : MoeClass ): Unit = superclass = s

    def getMethod ( name : String ): MoeMethod = {
        if ( hasMethod( name ) ) return methods( name )
        if ( hasSuperclass     ) return superclass.getMethod( name )
        throw new Errors.MethodNotFound( name )
    }

    def hasMethod ( name : String ): Boolean = {
        if ( methods.contains( name ) ) return true
        if ( hasSuperclass            ) return superclass.hasMethod( name )
        false
    }

    override def toString (): String = {
        var out = "{ " + name + "-" + version + "-" + authority
        if (hasSuperclass) {
            out += " #extends " + superclass.toString()
        }    
        out + " }" 
    }
}