
import AssemblyKeys._ // put this at the top of the file

name := "moe"

version := "0.0.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "commons-cli" % "commons-cli" % "1.2" % "compile"

libraryDependencies += "jline" % "jline" % "1.0"

// execute "run" task in a forked process
fork in run := true

// forward standard input to forked "run" process
connectInput in run := true

// send output to the build's standard output and error
outputStrategy := Some(StdoutOutput)

assemblySettings