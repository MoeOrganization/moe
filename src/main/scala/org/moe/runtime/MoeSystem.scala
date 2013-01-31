package org.moe.runtime

class MoeSystem(
   private var STDOUT : java.io.PrintStream    = Console.out,
   private var STDIN  : java.io.BufferedReader = Console.in,
   private var STDERR : java.io.PrintStream    = Console.err
 ) {

  def getSTDIN  = STDIN
  def getSTROUT = STDOUT
  def getSTDERR = STDERR

  def getEnv = sys.env

  def exit ()            = sys.exit()
  def exit (status: Int) = sys.exit(status)
}

/*

NOTES:

This object is meant to wrap some basic runtime
features so that we can control/configure it
within out runtime. This is just the basics for
now, but eventually this should be the primary
way in which our runtime interfaces with it's
host runtime.

*/