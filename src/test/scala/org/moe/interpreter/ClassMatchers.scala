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

   class ClassExtendsClassMatcher[A](superclass: A) extends Matcher[MoeOpaque] {
     def apply(left: MoeOpaque) = {
       val klass = left.getAssociatedClass.getOrElse(throw new Exception("Missing class"))

       val superclass_name = superclass match {
         case c: MoeClass => c.getName
         case Some(c: MoeClass) => c.getName
         case s: String => s
       }
       val failure_msg     = klass.getName + " was not a class of " + superclass_name
       val neg_failure_msg = klass.getName + " was a class of " + superclass_name
       val check = superclass match {
         case c: MoeClass => klass.isClassOf(c)
         case s: String   => klass.isClassOf(s)
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

   class ClassHasAttributeMatcher(attr: String) extends Matcher[MoeOpaque] {
     def apply(left: MoeOpaque) = {
       val klass = left.getAssociatedClass.getOrElse(throw new Exception("Missing class"))
       val failure_msg     = klass.getName + " did not have attribute " + attr
       val neg_failure_msg = klass.getName + " had attribute " + attr
       val check = klass.hasAttribute(attr)
       MatchResult(
         check,
         "Class " + failure_msg,
         "Class " + neg_failure_msg,
         "class " + failure_msg,
         "class " + neg_failure_msg
       )
     }
   }

   def haveAttribute(attr: String) = new ClassHasAttributeMatcher(attr)

   class ClassHasMethodMatcher(meth: String) extends Matcher[MoeOpaque] {
     def apply(left: MoeOpaque) = {
       val klass = left.getAssociatedClass.getOrElse(throw new Exception("Missing class"))
       val failure_msg     = klass.getName + " did not have method " + meth
       val neg_failure_msg = klass.getName + " had method " + meth
       val check = klass.hasMethod(meth)
       MatchResult(
         check,
         "Class " + failure_msg,
         "Class " + neg_failure_msg,
         "class " + failure_msg,
         "class " + neg_failure_msg
       )
     }
   }

   def haveMethod(attr: String) = new ClassHasMethodMatcher(attr)
 }

 object ClassMatchers extends ClassMatchers
