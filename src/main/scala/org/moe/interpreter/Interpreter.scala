package org.moe.interpreter

import org.moe.runtime._
import org.moe.runtime.nativeobjects._
import org.moe.ast._
import org.moe.parser._

import org.moe.interpreter.guts._

class Interpreter {

  def callMethod(invocant: MoeObject, method: String, args: List[MoeObject], klass: String = null) =
    invocant.callMethod(
      invocant.getAssociatedClass.getOrElse(
        throw new MoeErrors.ClassNotFound(Option(klass).getOrElse(invocant.getClassName))
      ).getMethod(method).getOrElse(
        throw new MoeErrors.MethodNotFound("method " + method + "> missing in class " + Option(klass).getOrElse(invocant.getClassName))
      ),
      args
    )

  def zipVars (r: MoeRuntime, names: List[String], expressions: List[MoeObject], f: ((String, MoeObject)) => Unit): Unit = {
    if (expressions.isEmpty) {
      names.foreach(f(_, r.NativeObjects.getUndef)) 
    } else if (names.isEmpty) {
      ()
    } else {
      f(names.head, expressions.headOption.getOrElse(r.NativeObjects.getUndef))
      zipVars(r, names.tail, expressions.tail, f)
    }
  }

  var compiler  : PartialFunction[(MoeEnvironment, AST), MoeObject] = _
  var evaluator : PartialFunction[(MoeEnvironment, AST), MoeObject] = _

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
  }

  def compile  (env: MoeEnvironment, node: AST): MoeObject = compiler(env -> node) 
  def evaluate (env: MoeEnvironment, node: AST): MoeObject = evaluator(env -> node)

  def eval(runtime: MoeRuntime, env: MoeEnvironment, node: AST): MoeObject = {
    prepare(runtime)
    if (compiler.isDefinedAt(env -> node)) {
      return compile(env, node)
    }
    if (evaluator.isDefinedAt(env -> node)) {
      return evaluate(env, node)
    }
    throw new MoeErrors.UnknownNode(node.toString)
  }

}
