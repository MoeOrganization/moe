package org.moe.interpreter

import scala.collection.mutable.Stack

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._
import org.moe.parser._

import org.moe.interpreter.guts._

class MoeInterpreter {

  private val callstack: Stack[MoeStackFrame] = new Stack[MoeStackFrame]()

  def getCallStack: List[MoeStackFrame] = callstack.toList
  def pushCallStack (frame: MoeStackFrame) = callstack.push(frame)
  def popCallStack: MoeStackFrame = callstack.pop 
  def peakCallStack: MoeStackFrame = callstack.top  

  private var compiler  : PartialFunction[(MoeEnvironment, AST), MoeObject] = _
  private var evaluator : PartialFunction[(MoeEnvironment, AST), MoeObject] = _
  private var both      : PartialFunction[(MoeEnvironment, AST), MoeObject] = _

  def prepare(runtime: MoeRuntime) = {
    if (compiler == null) {
      compiler = CompilationUnits(this, runtime).orElse(
        Signatures.declaration(this, runtime).orElse(
        Packages.declaration(this, runtime).orElse(
        Variables.declaration(this, runtime).orElse(
        Subroutines.declaration(this, runtime).orElse(
        Classes.declaration(this, runtime)
      )))))
    }
    if (evaluator == null) {
      evaluator = CompilationUnits(this, runtime).orElse(
        Literals(this, runtime).orElse(
        Operators(this, runtime).orElse(
        ElementAccess(this, runtime).orElse(
        Statements(this, runtime).orElse(
        Variables(this, runtime).orElse(
        Subroutines(this, runtime).orElse(
        Classes(this, runtime)
      )))))))
    }
    if (both == null) {
      both = compiler.orElse(evaluator)
    }
  }

  def compile  (env: MoeEnvironment, node: AST): MoeObject = compiler(env -> node) 
  def evaluate (env: MoeEnvironment, node: AST): MoeObject = evaluator(env -> node)

  def canCompile  (env: MoeEnvironment, node: AST): Boolean = compiler.isDefinedAt(env -> node)
  def canEvaluate (env: MoeEnvironment, node: AST): Boolean = evaluator.isDefinedAt(env -> node)

  def compile_and_evaluate(env: MoeEnvironment, node: AST): MoeObject = {
    if (both.isDefinedAt(env -> node)) { 
      return both(env -> node)
    } else {
     throw new MoeErrors.UnknownNode(node.toString)
    }
  }

  def eval(runtime: MoeRuntime, env: MoeEnvironment, node: AST): MoeObject = {
    prepare(runtime)
    compile_and_evaluate(env, node)
  }

}
