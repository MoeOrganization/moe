package org.moe

import org.apache.commons.cli._;

import org.moe.ast._
import org.moe.parser._
import org.moe.interpreter._
import org.moe.runtime._

import java.io.File
import scala.io.Source

object Moe {

  def main (args: Array[String]): Unit = {
      val parser: CommandLineParser = new PosixParser()
      val options: Options          = new Options()

      options.addOption("h", false, "display this message")
      options.addOption("v", false, "display version information")
      options.addOption("u", false, "dump the AST after parsing")
      options.addOption("w", false, "enable many useful warnings")

      val e = new Option("e", "code to evaluate")
      e.setArgs(1)
      e.setArgName("program")
      options.addOption(e)

      /*
        TODO:

        Need to support some of these other options
        as well as look through perl itself to see
        what else we are missing.

        options.addOption("c", false, "check syntax only")
        options.addOption("d", false, "debug mode")
      */

      /*
        TODO:
        wrap this in try/catch block and
        watch for the various exceptions
        most importantly we need to tell
        the difference between a option
        parser error and a code execution
        error.
        - SL
      */

      val cmd: CommandLine = parser.parse(options, args);

      /*
        FIXME:
        I am sure this can be done more
        idiomatically, but just porting
        the java straight for now.
        - SL
      */
      if (cmd.hasOption("h")) {
          printHelp(options)
          return
      }

      val interpreter = new Interpreter()
      val runtime     = new MoeRuntime(
        warnings = cmd.hasOption("w")
      )

      if (cmd.hasOption("v")) {
          printVersionInformation(runtime)
          return
      }

      val dumpAST = cmd.hasOption("u")

      if (cmd.hasOption("e")) {
          val code: String = cmd.getOptionValue("e")
          REPL.evalLine(interpreter, runtime, code, dumpAST)
          return
      }
      else {
        val rest: Array[String] = cmd.getArgs()
        if (rest.length == 0) {
          REPL.enter(interpreter, runtime, dumpAST)
        } else {
          // TODO: invocation arguments
          val path = rest(0)

          val source = Source.fromFile(path).mkString
          try {
            val nodes = MoeParsers.parseFromEntry(source)
            val ast = CompilationUnitNode(
              ScopeNode(nodes)
            )
            if (dumpAST) {
              println(Serializer.toJSON(ast))
            }
            interpreter.eval(runtime, runtime.getRootEnv, ast)
          }
          catch {
            case e: Exception => System.err.println(e)
          }
        }
      }
  }

  def printHelp (options: Options): Unit = {
    val formatter: HelpFormatter = new HelpFormatter();
    formatter.printHelp("moe", options);
  }

  // NOTE: this likely needs work - SL
  def printVersionInformation (runtime: MoeRuntime): Unit = println(
    "\n" +
    "This is Moe v" + runtime.getVersion + "\n" +
    "\n" +
    "This software is copyright (c) 2013 by Infinity Interactive, Inc.\n" +
    "\n" +
    "This is free software; you can redistribute it and/or \n" +
    "modify it under the terms specified in the LICENSE file.\n"
  )

  /*
    TODO:
    - would be nice to have line editing capabilities
      - this presents a problem under sbt since sbt wants
        to own the line editing capabilities
  */
  object REPL {
    def enter (interpreter: Interpreter, runtime: MoeRuntime, dumpAST: Boolean = false): Unit = {
      while (true) {
        val line = readLine("> ")
        if (line != null && line != "exit") {
          evalLine(interpreter, runtime, line, dumpAST)
        }
        else {
          return
        }
      }
    }

    def evalLine(interpreter: Interpreter, runtime: MoeRuntime, line: String, dumpAST: Boolean = false) = {
      try {
        val nodes = MoeParsers.parseFromEntry(line)
        val ast = CompilationUnitNode(
          ScopeNode(nodes)
        )
        if (dumpAST) {
          println(Serializer.toJSON(ast))
        }
        val result = interpreter.eval(runtime, runtime.getRootEnv, ast)
        println(result.toString)
      }
      catch {
        case e: Exception => System.err.println(e)
      }
    }
  }

}
