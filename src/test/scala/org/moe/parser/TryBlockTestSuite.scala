package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class TryBlockTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils {

  test("... a basic try block") {
    val result = interpretCode("try { 100 }")
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch block") {
    val result = interpretCode("try { 100 } catch (Exception $e) { 'yup' }")
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch-catch block") {
    val result = interpretCode("try { 100 } " +
      "catch (Exception $e) { 'yup' }" +
      "catch (OtherException $e) { 'indeed' }"
    )
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }

  test("... a basic try-catch-catch-finally block") {
    val result = interpretCode("try { 100 } " +
      "catch (Exception $e) { 'yup' }" +
      "catch (Other::Exception $e) { 'indeed' }" +
      "finally { 200 }"
    )
    result match {
      case _: MoeObject => assert(true) // stub
      case _ => assert(false)
    }
  }


}
