package org.moe.runtime

class MoeVariable ( 
        private val name  : String,
        private var value : MoeObject
    ) {

    def getName  (): String    = name 
    def getValue (): MoeObject = value
    def setValue ( v : MoeObject ) = value = v
}
