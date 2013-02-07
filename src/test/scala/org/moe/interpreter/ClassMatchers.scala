package org.moe.interpreter

import org.moe.runtime._

import org.scalatest.matchers._

 trait ClassMatchers extends {
   class PackageHasClassMatcher(klass: String) extends Matcher[MoePackage] {

     def apply(left: MoePackage) = {
       val failure_msg     = left.getName + " did not have class " + klass
       val neg_failure_msg = left.getName + " had class " + klass
       MatchResult(
         left.hasClass(klass),
         "Package " + failure_msg,
         "Package " + neg_failure_msg,
         "package " + failure_msg,
         "package " + neg_failure_msg
       )
     }
   }
   def haveClass(klass: String) = new PackageHasClassMatcher(klass)

   class ClassExtendsClassMatcher[A](superclass: A) extends Matcher[MoeClass] {
     def apply(left: MoeClass) = {
       val superclass_name = superclass match {
         case c: MoeClass => c.getName
         case s: String => s
       }
       val failure_msg     = left.getName + " was not a class of " + superclass_name
       val neg_failure_msg = left.getName + " was a class of " + superclass_name
       val check = superclass match {
         case c: MoeClass => left.isClassOf(c)
         case s: String   => left.isClassOf(s)
         case _           => false
       }
       MatchResult(
         check,
         "Class " + failure_msg,
         "Class " + neg_failure_msg,
         "class " + failure_msg,
         "class " + neg_failure_msg
       )
     }
   }

   def extendClass(superclass: MoeClass) = new ClassExtendsClassMatcher[MoeClass](superclass)
   def extendClass(superclass: String) = new ClassExtendsClassMatcher[String](superclass)
 }

 object ClassMatchers extends ClassMatchers
