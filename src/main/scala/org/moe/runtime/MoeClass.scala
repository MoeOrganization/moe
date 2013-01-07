package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeClass ( private val name : String ) {

    private var version    : String   = _
    private var authority  : String   = _ 
    private var superclass : MoeClass = _

    private val methods    = new HashMap[ String, MoeMethod    ]()
    private val attributes = new HashMap[ String, MoeAttribute ]()

    // the various alternate constructors ...

    def this ( name : String, superclass : MoeClass ) = {
        this( name )   
        setSuperclass( superclass )
    }

    def this ( name : String, version : String ) = {
        this( name )    
        setVersion( version )
    }

    def this ( name : String, version : String, superclass : MoeClass ) = {
        this( name, version )   
        setSuperclass( superclass )
    }

    def this ( name : String, version : String, authority : String ) = {
        this( name, version )    
        setAuthority( authority )
    }

    def this ( name : String, version : String, authority : String, superclass : MoeClass ) = {
        this( name, version, authority )   
        setSuperclass( superclass )
    }    

    // Identity ...

    def getName      (): String = name
    def getVersion   (): String = version
    def getAuthority (): String = authority

    def setVersion   ( v : String ): Unit = version = v
    def setAuthority ( a : String ): Unit = authority = a

    // Superclass ...

    def getSuperclass (): MoeClass = superclass
    def hasSuperclass (): Boolean  = superclass != null
    def setSuperclass ( s : MoeClass ): Unit = superclass = s

    def getMRO (): List[ MoeClass ] = {
        if ( superclass == null ) List( this )
        else 
            this :: superclass.getMRO() 
    }

    // Attributes

    def addAttribute ( attribute : MoeAttribute ): Unit = {
        attributes += ( attribute.getName() -> attribute )
    }

    def getAttribute ( name : String ): MoeAttribute = {
        if ( hasAttribute( name ) ) return attributes( name )
        if ( hasSuperclass        ) return superclass.getAttribute( name )
        throw new Runtime.Errors.AttributeNotFound( name )
    }

    def hasAttribute ( name : String ): Boolean = {
        if ( attributes.contains( name ) ) return true
        if ( hasSuperclass               ) return superclass.hasAttribute( name )
        false
    }

    private def collectAllAttributes (): HashMap[ String, MoeAttribute ] = {
        if ( superclass == null ) attributes.clone()
        else 
            superclass.collectAllAttributes() ++ attributes
    }

    // Instances 

    def newInstance (): MoeObject = {
        val instance = new MoeObject( this )
        collectAllAttributes().values.foreach(
            ( attr ) => instance.setValue( attr.getName(), null )
        )
        instance
    }

    // Methods ...

    def addMethod ( method : MoeMethod ): Unit = {
        methods += ( method.getName() -> method )
    }

    def getMethod ( name : String ): MoeMethod = {
        if ( hasMethod( name ) ) return methods( name )
        if ( hasSuperclass     ) return superclass.getMethod( name )
        throw new Runtime.Errors.MethodNotFound( name )
    }

    def hasMethod ( name : String ): Boolean = {
        if ( methods.contains( name ) ) return true
        if ( hasSuperclass            ) return superclass.hasMethod( name )
        false
    }

    // Utils ...

    override def toString (): String = {
        var out = "{ " + name + "-" + version + "-" + authority
        if (hasSuperclass) out += " #extends " + superclass.toString()
        out + " }" 
    }
}