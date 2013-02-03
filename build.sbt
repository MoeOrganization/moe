name := "moe"

version := "0.0.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "commons-cli" % "commons-cli" % "1.2" % "compile"
