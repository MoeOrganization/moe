package org.moe

import org.moe.ast._
import org.moe.parser._
import org.moe.interpreter._
import org.moe.runtime._

object Moe {

  object REPL {
    def evalLine(line: String) = {
      try {
        val nodes = List(MoeParsers.parseFromEntry(line))
        val ast = CompilationUnitNode(
          ScopeNode(
            StatementsNode(nodes)
          )
        )

        val result = Interpreter.eval(MoeRuntime.getRootEnv, ast)
        println(result.toString)
      }
      catch {
        case e: Exception => System.err.println(e)
      }
    }
  }

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
