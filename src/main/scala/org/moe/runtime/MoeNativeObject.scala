package org.moe.runtime

import scala.collection.mutable.HashMap

trait MoeNativeObject extends MoeObject {
    // NOTE:
    // This is kinda ugly, I need to rethink
    // this one here, but lets leave it for
    // now until this is more finished
    // - SL
    override def hasValue ( name : String ): Boolean   = 
        throw new Runtime.Errors.MethodNotAllowed( "MoeNativeObject::hasValue" )
    override def getValue ( name : String ): MoeObject = 
        throw new Runtime.Errors.MethodNotAllowed( "MoeNativeObject::getValue" )
    override def setValue ( name : String, value : MoeObject ): Unit = 
        throw new Runtime.Errors.MethodNotAllowed( "MoeNativeObject::setValue" )
}

class MoeIntObject ( private val value : Int ) extends MoeObject with MoeNativeObject {
    def this ( v : Int, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): Int = value
    override def isFalse (): Boolean = value == 0      
}

class MoeFloatObject ( private val value : Double ) extends MoeObject with MoeNativeObject {
    def this ( v : Double, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): Double = value
    override def isFalse (): Boolean = value == 0              
}

class MoeStringObject ( private val value : String ) extends MoeObject with MoeNativeObject {   
    def this ( v : String, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): String = value
    override def isFalse (): Boolean = value == ""      
}

class MoeBooleanObject ( private val value : Boolean ) extends MoeObject with MoeNativeObject {  
    def this ( v : Boolean, c : MoeClass ) = { this( v ); setAssociatedClass( c ) } 
    def getNativeValue (): Boolean = value
    override def isFalse (): Boolean = value == false      
}

class MoeNullObject extends MoeObject with MoeNativeObject {   
    def this ( c : MoeClass ) = { this(); setAssociatedClass( c ) }
    def getNativeValue (): AnyRef = null   
    override def isFalse (): Boolean = true      
    override def isUndef (): Boolean = true     
}



