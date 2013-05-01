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

  def eval(runtime: MoeRuntime, env: MoeEnvironment, node: AST): MoeObject = {

    val evaluator = 
        CompilationUnits(this, runtime, env).orElse(
        Literals(this, runtime, env).orElse(
        Operators(this, runtime, env).orElse(
        ElementAccess(this, runtime, env).orElse(
        Statements(this, runtime, env).orElse(

          Signatures.declaration(this, runtime, env).orElse(
          Packages.declaration(this, runtime, env).orElse(

            Variables(this, runtime, env).orElse(
              Variables.declaration(this, runtime, env).orElse(
            Subroutines(this, runtime, env).orElse(
              Subroutines.declaration(this, runtime, env).orElse(
            Classes(this, runtime, env).orElse(
              Classes.declaration(this, runtime, env)
            )))))
          ))
        )))))

    evaluator(node)
  }

}
