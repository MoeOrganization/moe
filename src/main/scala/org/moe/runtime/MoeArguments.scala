package org.moe.runtime

class MoeArguments(
    private val args: List[MoeObject] = List(),
    private val invocant: Option[MoeObject] = None
  ) {

  private var consumed_count = 0

  def getInvocant: Option[MoeObject] = invocant
  def hasInvocant: Boolean = invocant.isDefined

  def getArgCount: Int = args.length

  def getArgAt(i: Int): Option[MoeObject] = {
    if (i < args.length) {
      consumed_count = consumed_count + 1
      Some(args(i)) 
    } else {
      None
    }
  }
  
  def slurpArgsAt(i: Int): List[MoeObject] = {
    consumed_count = args.length
    args.drop(i)
  }

  def consumedArgCount = consumed_count
  def wereAllArgsConsumed: Boolean = consumed_count == args.length
}

/**

package Moe::Runtime {
    
    class MoeArguments {
        
        has @!args = [];
        has $!invocant; 

        method get_invocant { $!invocant }
        method has_invocant { $!invocant.defined }

        method get_arg_count { @!args.length }

        method get_arg_at ( $idx ) { @!args[$idx] }

        method slurp_args_at ( $idx ) {
            @!args.range( $idx, @!args.length )     
        }
    }
}

**/