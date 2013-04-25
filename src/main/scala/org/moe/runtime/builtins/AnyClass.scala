package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Any
  */
object AnyClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val anyClass = r.getCoreClassFor("Any").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Any")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeObject = e.getCurrentInvocant.getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Any, Object

    // output methods

    anyClass.addMethod(
      new MoeMethod(
        "say",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.say(self(e))
            getUndef
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "print",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.print(self(e))
            getUndef
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "warn",
        new MoeSignature(), 
        env, 
        { (e) => 
            r.warn(self(e))
            getUndef
        }
      )
    )

    // type checking

    anyClass.addMethod(
      new MoeMethod(
        "isa",
        new MoeSignature(List(new MoePositionalParameter("$name"))), 
        env, 
        (e) => getBool(self(e).isInstanceOf(e.getAs[MoeStrObject]("$name").get.unboxToString.get))
      )
    )

    // definedness

    anyClass.addMethod(
      new MoeMethod(
        "defined",
        new MoeSignature(), 
        env, 
        (e) => getBool(!self(e).isUndef)
      )
    )

    // logical ops

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<!>",
        new MoeSignature(), 
        env, 
        (e) => if (self(e).isTrue) getFalse else getTrue
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "not",
        new MoeSignature(), 
        env, 
        (e) => if (self(e).isTrue) getFalse else getTrue
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "infix:<&&>",
        new MoeSignature(List(new MoeSlurpyParameter("@other"))), 
        env, 
        { (e) =>
            val inv = self(e)
            if (inv.isFalse)
              inv
            else
              e.getAs[MoeArrayObject]("@other").get.at_pos(r, getInt(0)) match {
                case deferred: MoeLazyEval => deferred.eval
                case other:    MoeObject   => other
              }
        }
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "infix:<||>",
        new MoeSignature(List(new MoeSlurpyParameter("@other"))), 
        env, 
        { (e) =>
            val inv = self(e)
            if (inv.isTrue)
              inv
            else
              e.getAs[MoeArrayObject]("@other").get.at_pos(r, getInt(0)) match {
                case deferred: MoeLazyEval => deferred.eval
                case other:    MoeObject   => other
              }
        }
      )
    )

    // ternary operator

    anyClass.addMethod(
      new MoeMethod(
        "infix:<?:>",
        new MoeSignature(List(new MoeSlurpyParameter("@exprs"))),
        env,
        { (e) =>
            val inv = self(e)
            if (inv.isTrue)
              e.getAs[MoeArrayObject]("@exprs").get.at_pos(r, getInt(0)) match {
                case deferredExpr: MoeLazyEval => deferredExpr.eval
                case expr:         MoeObject   => expr
              }
            else
              e.getAs[MoeArrayObject]("@exprs").get.at_pos(r, getInt(1)) match {
                case deferredExpr: MoeLazyEval => deferredExpr.eval
                case expr:         MoeObject   => expr
              }
        }
      )
    )

    // coercion

    anyClass.addMethod(
      new MoeMethod(
        "Str",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeStrContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<~>",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeStrContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "Int",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeIntContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "Num",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeNumContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<+>",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeNumContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "Bool",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeBoolContext()))
      )
    )

    anyClass.addMethod(
      new MoeMethod(
        "prefix:<?>",
        new MoeSignature(),
        env,
        (e) => self(e).coerce(r, Some(MoeBoolContext()))
      )
    )

    /**
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Any.pm
     */
  }
}
