package org.moe.runtime.builtins

import org.moe.runtime._
import org.moe.runtime.nativeobjects._

/**
  * setup class Str
  */
object StrClass {

  def apply(r: MoeRuntime): Unit = {
    val env      = new MoeEnvironment(Some(r.getCorePackage.getEnv))
    val strClass = r.getCoreClassFor("Str").getOrElse(
      throw new MoeErrors.MoeStartupError("Could not find class Str")
    )

    import r.NativeObjects._

    def self(e: MoeEnvironment): MoeStrObject = e.getCurrentInvocantAs[MoeStrObject].getOrElse(
      throw new MoeErrors.InvocantNotFound("Could not find invocant")
    )

    // MRO: Str, Scalar, Any, Object

    strClass.addSubMethod(
      new MoeMethod(
        "CREATE",
        new MoeSignature(List(new MoeOptionalParameter("$string"))),
        env,
        (e) => e.get("$string") match {
          case Some(s: MoeStrObject) => s.copy
          case _                     => getStr("")
        }
      )
    )

    // increment/decrement

    strClass.addMethod(
      new MoeMethod(
        "prefix:<++>",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = self(e)
            inv.increment(r)
            inv
        }
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "postfix:<++>",
        new MoeSignature(),
        env,
        { (e) =>
            val inv = self(e)
            val old = getStr(inv.unboxToString.get)
            inv.increment(r)
            old
        }
      )
    )

    // relational operators

    strClass.addMethod(
      new MoeMethod(
        "infix:<lt>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than(r, e.get("$other").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "infix:<gt>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than(r, e.get("$other").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "infix:<le>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).less_than_or_equal_to(r, e.get("$other").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "infix:<ge>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).greater_than_or_equal_to(r, e.get("$other").get)
      )
    )

    // equality operators

    strClass.addMethod(
      new MoeMethod(
        "infix:<eq>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).equal_to(r, e.get("$other").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "infix:<ne>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).not_equal_to(r, e.get("$other").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "infix:<cmp>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).compare_to(r, e.get("$other").get)
      )
    )

    // methods

    strClass.addMethod(
      new MoeMethod(
        "chomp",
        new MoeSignature(), 
        env, 
        (e) => self(e).chomp(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "chop",
        new MoeSignature(), 
        env, 
        (e) => self(e).chop(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "uc",
        new MoeSignature(), 
        env, 
        (e) => self(e).uc(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "lc",
        new MoeSignature(), 
        env, 
        (e) => self(e).lc(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "ucfirst",
        new MoeSignature(), 
        env, 
        (e) => self(e).ucfirst(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "lcfirst",
        new MoeSignature(), 
        env, 
        (e) => self(e).lcfirst(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "length",
        new MoeSignature(), 
        env, 
        (e) => self(e).length(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "reverse",
        new MoeSignature(), 
        env, 
        (e) => self(e).reverse(r)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "split",
        new MoeSignature(List(new MoePositionalParameter("$string"))), 
        env, 
        (e) => self(e).split(
            r, 
            e.getAs[MoeStrObject]("$string").get
        )
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "concat",
        new MoeSignature(List(new MoeSlurpyParameter("@strings"))),
        env, 
        (e) => self(e).concatAll(r, e.getAs[MoeArrayObject]("@strings").get)
      )
    )        

    strClass.addMethod(
      new MoeMethod(
        "pad",
        new MoeSignature(List(new MoePositionalParameter("$amount"))),
        env, 
        (e) => self(e).pad(r, e.getAs[MoeIntObject]("$amount").get)
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "rpad",
        new MoeSignature(List(new MoePositionalParameter("$amount"))),
        env, 
        (e) => self(e).rpad(r, e.getAs[MoeIntObject]("$amount").get)
      )
    )

    // concatenation

    strClass.addMethod(
      new MoeMethod(
        "infix:<~>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).concat(r, e.getAs[MoeStrObject]("$other").get)
      )
    )

    // repetition

    strClass.addMethod(
      new MoeMethod(
        "infix:<x>",
        new MoeSignature(List(new MoePositionalParameter("$other"))),
        env,
        (e) => self(e).repeat(r, e.getAs[MoeIntObject]("$other").get)
      )
    )

    // regex matching
    strClass.addMethod(
      new MoeMethod(
        "match",
        new MoeSignature(List(new MoePositionalParameter("$rx"),
                              new MoeOptionalParameter("$flags"))),
        env,
        (e) => {
          e.get("$rx") match {
            case Some(rx: MoeRegexObject) => self(e).matches(r, rx)
            case Some(s:  MoeStrObject)   => self(e).matches(r, s)
            case _                        => throw new MoeErrors.IncompatibleType("Str or Regex expected")
          }
        }
      )
    )

    strClass.addMethod(
      new MoeMethod(
        "subst",
        new MoeSignature(List(new MoePositionalParameter("$rx"),
                              new MoePositionalParameter("$replacement"),
                              new MoeOptionalParameter("$flags"))),
        env,
        (e) => {
          val replacement = e.getAs[MoeStrObject]("$replacement").get
          val flags     = e.get("$flags") match {
            case Some(s: MoeStrObject) => s.copy
            case _                     => getStr("")
          }
          e.get("$rx") match {
            case Some(rx: MoeRegexObject) => self(e).subst(r, rx, replacement, flags)
            case Some(s:  MoeStrObject)   => self(e).subst(r, s, replacement, flags)
            case _                        => throw new MoeErrors.IncompatibleType("Str or Regex expected")
          }
        }
      )
    )

    /**
     * List of Operators to support:
     * - infix:<.>
     * - infix:<x>
     *
     * List of Methods to support:
     * - index ($substring, ?$position)
     * - rindex ($substring, ?$position)
     * - sprintf ($format, @items)
     * - substr ($offset, ?$length)
     * - quotemeta
     *
     * See the following for details:
     * - https://metacpan.org/release/autobox-Core
     * - https://github.com/rakudo/rakudo/blob/nom/src/core/Str.pm
     */

  }

}
