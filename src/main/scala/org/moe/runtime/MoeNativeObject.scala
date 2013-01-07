package org.moe.runtime

import scala.collection.mutable.HashMap

class MoeNativeObject extends MoeObject {

    private var value : AnyRef   = _
    private var klass : MoeClass = _    

    def this ( v : Int     ) = { this(); value = v.asInstanceOf[AnyRef] }
    def this ( v : Float   ) = { this(); value = v.asInstanceOf[AnyRef] }
    def this ( v : String  ) = { this(); value = v.asInstanceOf[AnyRef] }
    def this ( v : Boolean ) = { this(); value = v.asInstanceOf[AnyRef] }

    def this ( v : Int, k : MoeClass ) = {
        this( v )
        klass = k
    }

    def this ( v : Float, k : MoeClass ) = {
        this( v )
        klass = k
    }

    def this ( v : String, k : MoeClass ) = {
        this( v )
        klass = k
    }

    def this ( v : Boolean, k : MoeClass ) = {
        this( v )
        klass = k
    }    

    def getNativeValue (): AnyRef = value

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