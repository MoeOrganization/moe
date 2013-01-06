package org.moe.runtime

class MoeAttribute ( 
        private val name    : String,
        private val default : MoeObject
    ) {

    def getName    (): String    = name 
    def getDefault (): MoeObject = default
}