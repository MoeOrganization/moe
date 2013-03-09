package org.moe.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

import org.moe.runtime._
import org.moe.interpreter._
import org.moe.ast._
import org.moe.parser._

class SimpleExpressionTestSuite extends FunSuite with BeforeAndAfter with ParserTestUtils with ShouldMatchers {

  val delta = 1e-10

  test("... literal int addition #1 ... [2+2]") {
    val result = interpretCode("2+2")
    result.unboxToInt.get should equal (4)
  }

  test("... literal int addition #2 ... [4+-2]") {
    val result = interpretCode("4+-2")
    result.unboxToInt.get should equal (2)
  }

  test("... literal int addition #3 ... [-10 + 100]") {
    val result = interpretCode("-10 + 100")
    result.unboxToInt.get should equal (90)
  }

  test("... literal int addition #4 ... [-7 + -9]") {
    val result = interpretCode("-7 + -9")
    result.unboxToInt.get should equal (-16)
  }

  test("... literal int addition #5 ... [-63 + +2]") {
    val result = interpretCode("-63 + +2")
    result.unboxToInt.get should equal (-61)
  }

  test("... literal int addition #6 ... [4 + -1]") {
    val result = interpretCode("4 + -1")
    result.unboxToInt.get should equal (3)
  }

  test("... literal int addition #7 ... [-1 + 1]") {
    val result = interpretCode("-1 + 1")
    result.unboxToInt.get should equal (0)
  }

  test("... literal int addition #8 ... [+29 + -29]") {
    val result = interpretCode("+29 + -29")
    result.unboxToInt.get should equal (0)
  }

  test("... literal int addition #9 ... [-1 + 4]") {
    val result = interpretCode("-1 + 4")
    result.unboxToInt.get should equal (3)
  }

  test("... literal int addition #10 ... [+4 + -17]") {
    val result = interpretCode("+4 + -17")
    result.unboxToInt.get should equal (-13)
  }

  test("... literal int addition #11 ... [2+2.3]") {
    val result = interpretCode("2+2.3")
    result.unboxToDouble.get should be (4.3 plusOrMinus delta)
  }

  test("... literal int addition #12 ... [4+-2.3]") {
    val result = interpretCode("4+-2.3")
    result.unboxToDouble.get should be (1.7 plusOrMinus delta)
  }

  // subtraction

  test("... literal int subtraction #1 ... [2-5]") {
    val result = interpretCode("2-5")
    result.unboxToInt.get should equal (-3)
  }

  test("... literal int subtraction #2 ... [3 - 1]") {
    val result = interpretCode("3 - 1")
    result.unboxToInt.get should equal (2)
  }

  test("... literal int subtraction #3 ... [3 - 15]") {
    val result = interpretCode("3 - 15")
    result.unboxToInt.get should equal (-12)
  }

  test("... literal int subtraction #4 ... [3 - -7]") {
    val result = interpretCode("3 - -7")
    result.unboxToInt.get should equal (10)
  }

  test("... literal int subtraction #5 ... [-156 - 5]") {
    val result = interpretCode("-156 - 5")
    result.unboxToInt.get should equal (-161)
  }

  test("... literal int subtraction #6 ... [-156 - -5]") {
    val result = interpretCode("-156 - -5")
    result.unboxToInt.get should equal (-151)
  }

  test("... literal int subtraction #7 ... [-5 - -12]") {
    val result = interpretCode("-5 - -12")
    result.unboxToInt.get should equal (7)
  }

  test("... literal int subtraction #8 ... [-3 - -3]") {
    val result = interpretCode("-3 - -3")
    result.unboxToInt.get should equal (0)
  }

  test("... literal int subtraction #9 ... [15 - 15]") {
    val result = interpretCode("15 - 15")
    result.unboxToInt.get should equal (0)
  }

  test("... literal int subtraction #10 ... [2147483647 - 0]") {
    val result = interpretCode("2147483647 - 0")
    result.unboxToInt.get should equal (2147483647)
  }

  test("... literal int subtraction #11 ... [0 - -2147483647]") {
    val result = interpretCode("0 - -2147483647")
    result.unboxToInt.get should equal (2147483647)
  }

  test("... literal int subtraction #12 ... [2-5.4]") {
    val result = interpretCode("2-5.4")
    result.unboxToDouble.get should be (-3.4 plusOrMinus delta)
  }

  test("... literal int subtraction #2 ... [3 - -1.7]") {
    val result = interpretCode("3 - -1.7")
    result.unboxToDouble.get should be (4.7 plusOrMinus delta)
  }

  // multiplication

  test("... literal int multiplication #1 ... [3*3]") {
    val result = interpretCode("3*3")
    result.unboxToInt.get should equal (9)
  }

  test("... literal int multiplication #2 ... [1 * 3]") {
    val result = interpretCode("1 * 3")
    result.unboxToInt.get should equal (3)
  }

  test("... literal int multiplication #3 ... [-2 * 3]") {
    val result = interpretCode("-2 * 3")
    result.unboxToInt.get should equal (-6)
  }

  test("... literal int multiplication #4 ... [3 * -3]") {
    val result = interpretCode("3 * -3")
    result.unboxToInt.get should equal (-9)
  }

  test("... literal int multiplication #5 ... [-4 * -3]") {
    val result = interpretCode("-4 * -3")
    result.unboxToInt.get should equal (12)
  }

  test("... literal int multiplication #6 ... [3*3.2]") {
    val result = interpretCode("3*3.2")
    result.unboxToDouble.get should be (9.6 plusOrMinus delta)
  }

  test("... literal int multiplication #7 ... [1 * 3.7]") {
    val result = interpretCode("1 * 3.7")
    result.unboxToDouble.get should be (3.7 plusOrMinus delta)
  }

  // division

  test("... literal int division #1 ... [9/3]") {
    val result = interpretCode("9/3")
    result.unboxToDouble.get should equal (3)
  }

  test("... literal int division #2 ... [-2 / 2]") {
    val result = interpretCode("-2 / 2")
    result.unboxToDouble.get should equal (-1)
  }

  test("... literal int division #3 ... [3 / -2]") {
    val result = interpretCode("3 / -2")
    result.unboxToDouble.get should equal (-1.5)
  }

  test("... literal int division #4 ... [-4 / 2]") {
    val result = interpretCode("-4 / 2")
    result.unboxToDouble.get should equal (-2)
  }

  test("... literal int division #5 ... [-5 / -2]") {
    val result = interpretCode("-5 / -2")
    result.unboxToDouble.get should equal (2.5)
  }

  test("... literal int division #6 ... [9/3.2]") {
    val result = interpretCode("9/3.2")
    result.unboxToDouble.get should equal (2.8125)
  }

  test("... literal int division #7 ... [1/3.0]") {
    val result = interpretCode("1/3.0")
    result.unboxToDouble.get should be (0.3333333333333333 plusOrMinus delta)
  }

  // order of operations

  test("... order of operations, addition first") {
    val result = interpretCode("2+3*4")
    result.unboxToInt.get should equal (14)
  }

  test("... order of operations, multiplication first") {
    val result = interpretCode("2*3+4")
    result.unboxToInt.get should equal (10)
  }

  // modulo (% operator)

  test("... literal int modulo operation #1 ... [13 % 4]") {
    val result = interpretCode("13 % 4")
    result.unboxToInt.get should equal (1)
  }

  test("... literal int modulo operation #2 ... [-13 % 4]") {
    val result = interpretCode("-13 % 4")
    result.unboxToInt.get should equal (3)
  }

  test("... literal int modulo operation #3 ... [13 % -4]") {
    val result = interpretCode("13 % -4")
    result.unboxToInt.get should equal (-3)
  }

  test("... literal int modulo operation #4 ... [-13 % -4]") {
    val result = interpretCode("-13 % -4")
    result.unboxToInt.get should equal (-1)
  }

  // exponentiation

  test("... literal int exponentiation operation #1 ... [2**2]") {
    val result = interpretCode("2**2")
    result.unboxToInt.get should equal (4)
  }

  test("... literal int exponentiation operation #2 ... [2**2.2]") {
    val result = interpretCode("2**2.2")
    result.unboxToDouble.get should be (4.59479341998814 plusOrMinus delta)
  }

  test("... literal int exponentiation operation #3 ... [1**0]") {
    val result = interpretCode("1**0")
    result.unboxToInt.get should equal (1)
  }

  test("... literal int exponentiation operation #4 ... [1**1]") {
    val result = interpretCode("1**1")
    result.unboxToInt.get should equal (1)
  }

  test("... literal int exponentiation operation #5 ... [2**2**3]") {
    val result = interpretCode("2**2**3")
    result.unboxToInt.get should equal (256)
  }

  test("... literal int exponentiation operation #6 ... [2**3**4]") {
    val result = interpretCode("2**3**4")
    result.unboxToInt.get should not equal (4096)
  }

  // relational operators

  test("... literal int relational operation #1 ... [1 < 2]") {
    val result = interpretCode("1 < 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #2 ... [2 < 1]") {
    val result = interpretCode("2 < 1")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #3 ... [1 < 2.5]") {
    val result = interpretCode("1 < 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #4 ... [1 > 2]") {
    val result = interpretCode("1 > 2")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #5 ... [2 > 1]") {
    val result = interpretCode("2 > 1")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #6 ... [1 > 2.5]") {
    val result = interpretCode("1 > 2.5")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #7 ... [1 <= 2]") {
    val result = interpretCode("1 <= 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #8 ... [2 <= 1]") {
    val result = interpretCode("2 <= 1")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #9 ... [1 <= 2.5]") {
    val result = interpretCode("1 <= 2.5")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #10 ... [1 >= 2]") {
    val result = interpretCode("1 >= 2")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #11 ... [2 >= 1]") {
    val result = interpretCode("2 >= 1")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #12 ... [1 >= 2.5]") {
    val result = interpretCode("1 >= 2.5")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #13 ... [2 == 2]") {
    val result = interpretCode("2 == 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #14 ... [2 != 2]") {
    val result = interpretCode("2 != 2")
    result.unboxToBoolean.get should equal (false)
  }

  test("... literal int relational operation #15 ... [2 >= 2]") {
    val result = interpretCode("2 >= 2")
    result.unboxToBoolean.get should equal (true)
  }

  test("... literal int relational operation #16 ... [2 <= 2]") {
    val result = interpretCode("2 <= 2")
    result.unboxToBoolean.get should equal (true)
  }

  // methods

  test("... literal int abs method ... [2->abs]") {
    val result = interpretCode("2->abs")
    result.unboxToInt.get should equal (2)
  }

  test("... literal int abs method ... [(-2)->abs]") {
    val result = interpretCode("(-2)->abs")
    result.unboxToInt.get should equal (2)
  }

  // trigonometric methods

  val sines = Map(
    -5 -> 0.958924274663138,
    -4 -> 0.756802495307928,
    -3 -> -0.141120008059867,
    -2 -> -0.909297426825682,
    -1 -> -0.841470984807897,
     0 -> 0,
     1 -> 0.841470984807897,
     2 -> 0.909297426825682,
     3 -> 0.141120008059867,
     4 -> -0.756802495307928,
     5 -> -0.958924274663138
  )

  for ((x: Int, y: Double) <- sines) {
    test("... literal int -- sin method ... [" + x + "->sin]") {
      val result = interpretCode(x + "->sin")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val cosines = Map(
    -5 -> 0.283662185463226,
    -4 -> -0.653643620863612,
    -3 -> -0.989992496600445,
    -2 -> -0.416146836547142,
    -1 -> 0.54030230586814,
     0 -> 1,
     1 -> 0.54030230586814,
     2 -> -0.416146836547142,
     3 -> -0.989992496600445,
     4 -> -0.653643620863612,
     5 -> 0.283662185463226
  )

  for ((x: Int, y: Double) <- cosines) {
    test("... literal int -- cos method ... [" + x + "->cos]") {
      val result = interpretCode(x + "->cos")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val tangents = Map(
    -5 -> 3.38051500624659,
    -4 -> -1.15782128234958,
    -3 -> 0.142546543074278,
    -2 -> 2.18503986326152,
    -1 -> -1.5574077246549,
     0 -> 0,
     1 -> 1.5574077246549,
     2 -> -2.18503986326152,
     3 -> -0.142546543074278,
     4 -> 1.15782128234958,
     5 -> -3.38051500624659
  )

  for ((x: Int, y: Double) <- tangents) {
    test("... literal int -- tan method ... [" + x + "->tan]") {
      val result = interpretCode(x + "->tan")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val arcSines = Map(
    -1 -> -1.5707963267949,
     0 -> 0,
     1 -> 1.5707963267949
  )

  for ((x: Int, y: Double) <- arcSines) {
    test("... literal int -- asin method ... [" + x + "->asin]") {
      val result = interpretCode(x + "->asin")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val arcCosines = Map(
    -1 -> 3.14159265358979,
     0 -> 1.5707963267949,
     1 -> 0
  )

  for ((x: Int, y: Double) <- arcCosines) {
    test("... literal int -- cos method ... [" + x + "->acos]") {
      val result = interpretCode(x + "->acos")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val arcTangents = Map(
    -1 -> -0.785398163397448,
     0 -> 0,
     1 -> 0.785398163397448
  )

  for ((x: Int, y: Double) <- arcTangents) {
    test("... literal int -- atan method ... [" + x + "->atan]") {
      val result = interpretCode(x + "->atan")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val expValues = Map(
    -2 -> 0.135335283236613,
    -1 -> 0.367879441171442,
     0 -> 1,
     1 -> 2.71828182845905,
     2 -> 7.38905609893065
  )

  for ((x: Int, y: Double) <- expValues) {
    test("... literal int -- exp method ... [" + x + "->exp]") {
      val result = interpretCode(x + "->exp")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val logValues = Map(
    1 -> 0,
    2 -> 0.693147180559945,
    3 -> 1.09861228866811,
    4 -> 1.38629436111989,
    5 -> 1.6094379124341,
    10 -> 2.30258509299405,
    100 -> 4.60517018598809
  )

  for ((x: Int, y: Double) <- logValues) {
    test("... literal int -- log method ... [" + x + "->log]") {
      val result = interpretCode(x + "->log")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }

  val sqrtValues = Map(
    1 -> 1,
    2 -> 1.4142135623731,
    3 -> 1.73205080756888,
    4 -> 2,
    5 -> 2.23606797749979,
    10 -> 3.16227766016838,
    100 -> 10
  )

  for ((x: Int, y: Double) <- sqrtValues) {
    test("... literal int -- sqrt method ... [" + x + "->sqrt]") {
      val result = interpretCode(x + "->sqrt")
      result.unboxToDouble.get should be (y plusOrMinus delta)
    }
  }
}
