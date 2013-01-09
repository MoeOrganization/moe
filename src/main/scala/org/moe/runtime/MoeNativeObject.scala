package org.moe.runtime

import scala.collection.mutable.HashMap

abstract class MoeNativeObject extends MoeObject {}

class MoeIntObject ( private val value : Int ) extends MoeNativeObject {
    def this ( v : Int, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): Int = value
    override def isFalse (): Boolean = value == 0      
}

class MoeFloatObject ( private val value : Double ) extends MoeNativeObject {
    def this ( v : Double, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): Double = value
    override def isFalse (): Boolean = value == 0              
}

class MoeStringObject ( private val value : String ) extends MoeNativeObject {   
    def this ( v : String, c : MoeClass ) = { this( v ); setAssociatedClass( c ) }
    def getNativeValue (): String = value
    override def isFalse (): Boolean = value == ""      
}

class MoeBooleanObject ( private val value : Boolean ) extends MoeNativeObject {  
    def this ( v : Boolean, c : MoeClass ) = { this( v ); setAssociatedClass( c ) } 
    def getNativeValue (): Boolean = value
    override def isFalse (): Boolean = value == false      
}

class MoeNullObject extends MoeNativeObject {   
    def this ( c : MoeClass ) = { this(); setAssociatedClass( c ) }
    def getNativeValue (): AnyRef = null   
    override def isFalse (): Boolean = true      
    override def isUndef (): Boolean = true     
}

class MoeArrayObject ( private val values : List[ MoeObject ] ) extends MoeNativeObject {  
    def this ( v : List[ MoeObject ], c : MoeClass ) = { this( v ); setAssociatedClass( c ) } 
    def getNativeValue (): List[ MoeObject ] = values
    override def isFalse (): Boolean = values.size == 0      
}

class MoeHashObject ( private val values : HashMap[ String, MoeObject ] ) extends MoeNativeObject {  
    def this ( v : HashMap[ String, MoeObject ], c : MoeClass ) = { this( v ); setAssociatedClass( c ) } 
    def getNativeValue (): HashMap[ String, MoeObject ] = values
    override def isFalse (): Boolean = values.size == 0      
}

class MoePairObject ( private val value : ( String, MoeObject ) ) extends MoeNativeObject {  
    def this ( v : ( String, MoeObject ), c : MoeClass ) = { this( v ); setAssociatedClass( c ) } 
    def getNativeValue (): ( String, MoeObject ) = value
}


