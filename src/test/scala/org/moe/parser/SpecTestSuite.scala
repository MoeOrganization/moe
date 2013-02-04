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

  test("... test through each spec file with a matching ast file") {
    val spec_path = "spec/syntax-examples"
    val f = new File(spec_path)
    for (cat <- f.list.map({p: String => new File(spec_path + "/" + p)}) if cat.isDirectory) {
      val cat_path = cat.getAbsolutePath
      for (moe_file <- cat.list.map({p: String => new File(cat_path + "/" + p)}) if moe_file.isFile) {
        val moe_path = moe_file.getAbsolutePath
        if (moe_path.endsWith(".mo")) {
          val ast_path = moe_path.replaceFirst(".mo$", ".ast")
          val ast_file = new File(ast_path)
          if (ast_file.isFile) {
            val moe_content = Source.fromFile(moe_path).mkString
            val ast_content = Source.fromFile(ast_path).mkString

            basicAST(Parser.parseStuff(moe_content)) match {
              case CompilationUnitNode(scope) => scope match {
                case ScopeNode(stmts) => {
                  assert(Serializer.toJSON(stmts).toString().trim == ast_content.trim)
                }
              }
            }

          }
        }
      }
    }
  }

}
