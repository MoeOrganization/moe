package org.moe.runtime

sealed abstract class MoeParameter(private val name: String) extends MoeObject {
  def getName    = name
  def getKeyName = name.drop(1)
}

case class MoePositionalParameter  (val n: String) extends MoeParameter(n)
case class MoeOptionalParameter    (val n: String) extends MoeParameter(n)
case class MoeSlurpyParameter      (val n: String) extends MoeParameter(n)
case class MoeNamedParameter       (val n: String) extends MoeParameter(n)
case class MoeSlurpyNamedParameter (val n: String) extends MoeParameter(n)

/**

package Moe::Runtime {

    role MoeParameter {
        
        has $!name;

        method get_name { $!name }
        method get_key_name {
            $!name.substr(1, $!name.length)
        }
    }

    class MoePositionalParameter  with MoeParameter {}
    class MoeOptionalParameter    with MoeParameter {}
    class MoeSlurpyParameter      with MoeParameter {}
    class MoeNamedParameter       with MoeParameter {}
    class MoeSlurpyNamedParameter with MoeParameter {}
}

**/