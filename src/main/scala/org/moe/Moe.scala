package org.moe

import org.apache.commons.cli._;

import org.moe.ast._
import org.moe.parser._
import org.moe.interpreter._
import org.moe.runtime._

object Moe {

  def main (args: Array[String]): Unit = {
      val parser: CommandLineParser = new PosixParser()
      val options: Options          = new Options()

      options.addOption("h", false, "display this message")
      options.addOption("v", false, "display version information")
      options.addOption("u", false, "dump the AST after parsing")

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

      val runtime = new MoeRuntime()

      if (cmd.hasOption("v")) {
          printVersionInformation(runtime)
          return
      }

      val dumpAST = cmd.hasOption("u")

      if (cmd.hasOption("e")) {
          val code: String = cmd.getOptionValue("e")
          REPL.evalLine(runtime, code, dumpAST)
          return
      }
      else {
          val rest: Array[String] = cmd.getArgs()
          if (rest.length == 0) {
              REPL.enter(runtime, dumpAST)
          } else {
              // TODO: ... read a file and execute it
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
    - would be good to be able to exit the REPL
    - would be nice to have line editing capabilities
      - this presents a problem under sbt since sbt wants
        to own the line editing capabilities
  */
  object REPL {
    def enter (runtime: MoeRuntime, dumpAST: Boolean = false): Unit = {
        var ok = true
        print("> ")
        while (ok) {
          val line = readLine()
          ok = line != null
          if (ok) {
            evalLine(runtime, line, dumpAST)
            print("> ")
          }
        }
    }

    def evalLine(runtime: MoeRuntime, line: String, dumpAST: Boolean = false) = {
      try {
        val nodes = List(MoeParsers.parseFromEntry(line))
        val ast = CompilationUnitNode(
          ScopeNode(
            StatementsNode(nodes)
          )
        )
        if (dumpAST) {
          println(ast.serialize)
        }
        val result = Interpreter.eval(runtime, runtime.getRootEnv, ast)
        println(result.toString)
      }
      catch {
        case e: Exception => System.err.println(e)
      }
    }
  }

}
