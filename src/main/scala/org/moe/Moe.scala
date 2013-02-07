package org.moe

import org.moe.ast._
import org.moe.parser._
import org.moe.interpreter._
import org.moe.runtime._

/** Factory for [[org.moe.Moe]] instances. */
object Moe {

  /** Read Eval Print Loop object for [[org.moe.Moe]] runtimes. */
  object REPL {
    /** Evaluates line of type String  and prints to stdio
    *
    * @param line to evaluate
    */
    def evalLine(line: String) = {
      try {
        val nodes = List(MoeParsers.parseFromEntry(line))
        val ast = CompilationUnitNode(
          ScopeNode(
            StatementsNode(nodes)
          )
        )

        val result = Interpreter.eval(Runtime.getRootEnv, ast)
        println(result.toString)
      }
      catch {
        case e: Exception => System.err.println(e)
      }
    }
  }

 /** Main loop for the REPL
  *
  * @param arguments in an array of strings
  */

  def main (args: Array[String]): Unit = {
    var ok = true
    print("> ")
    while (ok) {
      val line = readLine()
      ok = line != null
      if (ok) {
        REPL.evalLine(line)
        print("> ")
      }
    }
  }
}
