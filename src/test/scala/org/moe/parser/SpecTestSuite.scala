package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

import java.io.File
import scala.io.Source

class SpecTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  private def recurseDirectory (f : File)(action: (File) => Unit): Unit = {
    for ( i <- f.list.map({ p: String => new File( f.getAbsolutePath + "/" + p ) }) ) {
      if (i.isDirectory) {
        recurseDirectory(i)(action)
      }
      else {
        action(i)    
      }
    }
  }

  test("... test through each spec file with a matching ast file") {
    val spec_path = "spec/syntax-examples"

    recurseDirectory(new File(spec_path)) {
      f: File => {

        val moe_path = f.getAbsolutePath

        if (moe_path.endsWith(".mo")) {
          val ast_path = moe_path.replaceFirst(".mo$", ".ast")
          val ast_file = new File(ast_path)
          if (ast_file.isFile) {
            val moe_content = Source.fromFile(moe_path).mkString
            val ast_content = Source.fromFile(ast_path).mkString

            try { 
              basicAST(MoeParser.parseStuff(moe_content)) match {
                case CompilationUnitNode(scope) => scope match {
                  case ScopeNode(stmts, _) => {
                    assert(Serializer.toJSON(stmts).toString().trim === ast_content.trim)
                  }
                }
              }
            } catch {
              case e: java.lang.RuntimeException => throw new MoeErrors.MoeException("Parse failed on " + moe_path)
            }
            
          }
        }
      }
    }
  }

}
