package org.moe.runtime

import scala.collection.mutable.HashMap

class MoePackage ( 
        private val name : String, 
        private var env  : MoeEnvironment
    ) extends MoeObject {

    private var parent : MoePackage     = _

    private val klasses      = new HashMap[ String, MoeClass      ]()
    private val subs         = new HashMap[ String, MoeSubroutine ]()
    private val sub_packages = new HashMap[ String, MoePackage    ]()

    def this ( n : String, e : MoeEnvironment, p : MoePackage ) = {
        this( n, e )
        parent = p
    }

    def getName (): String  = name
    def isRoot  (): Boolean = parent == null 

    // Parent ...

    def getParent (): MoePackage = parent
    def setParent ( p : MoePackage ): Unit = parent = p

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

    // Classes ...

    def addClass ( klass : MoeClass ): Unit = {
        klasses += ( klass.getName() -> klass )
    }

    def getClass ( name : String ): MoeClass = {
        if ( hasClass( name ) ) return klasses( name )
        throw new Runtime.Errors.ClassNotFound( name )
    }

    def hasClass ( name : String ): Boolean = {
        if ( klasses.contains( name ) ) return true
        false
    }

    // SubPackages ...

    def addSubPackage ( pkg : MoePackage ): Unit = {
        sub_packages += ( pkg.getName() -> pkg )
    }

    def getSubPackage ( name : String ): MoePackage = {
        if ( hasSubPackage( name ) ) return sub_packages( name )
        throw new Runtime.Errors.PackageNotFound( name )
    }

    def hasSubPackage ( name : String ): Boolean = {
        if ( sub_packages.contains( name ) ) return true
        false
    }

}