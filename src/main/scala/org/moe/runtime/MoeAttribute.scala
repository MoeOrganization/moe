package org.moe.runtime

class MoeAttribute ( 
        private val name    : String,
        private val default : MoeObject
    ) extends MoeObject {

    def getName    (): String    = name 
    def getDefault (): MoeObject = default
}

/*

NOTES:

- the default value really should be cloned
  but the question is how to actually go about 
  this, so I am punting for the time being

*/