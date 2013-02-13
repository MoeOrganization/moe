package org.moe.interpreter

import org.moe.runtime._
import org.moe.ast._

trait Validator {

  import InterpreterUtils.walkAST

  // Validates the AST before interpreting to prevent
  // interpretable yet unwanted behavior
  def validate(runtime: MoeRuntime, env: MoeEnvironment, ast: AST): Unit = {
    var in_package_sub: Option[(String, Int)] = None
    walkAST(
      ast,
      { (node, level) =>

        // Refresh package-sub state before doing any validation
        in_package_sub match {
          case Some((cursub, sublevel)) =>
            // If we are on the same level or lower traversal level
            // after declaring the sub, then we are outside of the sub
            // due to the nature of DFS
            if (sublevel >= level) {
              in_package_sub = None
            }
          case None => /* noop */
        }

        // Validate sub-in-sub declaration
        node match {
          case SubroutineDeclarationNode(name, _, _) =>
            in_package_sub match {
              case Some((cursub, _)) => throw new MoeErrors.NotAllowed("sub " + name + " in sub " + cursub)
              case None => in_package_sub = Some(name, level)
            }
          case _ => /* noop */
        }
      }
    )
  }
}
