package lec2324.lecture

import lec2324.magicZone.ImplicitForPrint

object ScalaHigherOrderFunction extends App with ImplicitForPrint {

  val l = List(1, 2, 3)
//   l.map()

  def hof1(i: Int, f: Int => Int): Int = f(i)
  def quad(i: Int)                     = i * i
  val res                              = hof1(2, quad)
  res.print
  "-----------------".print

//  l.map((x: Int) => x * 2)
//  l.map(x => x * 2)
//  l.map(_ * 2)

  def hof3(i: Int): Int => Int = a => a * i

  // hof2(i) => a * i => f = a * i => f = a * 2
  // res2 = f = a * 2
  // res2(ai: Int) = ai * 2
  def hof2(i: Int): Int => Int = (a: Int) => a * i
  def res2: Int => Int         = hof2(3) // f(a: Int) = a * 3
  "-----------------".print
  res2.print
  "-----------------".print
  val x1 = res2(2) // f(2) = 2 * 3

  x1.print

  val res3 = hof2(2)
  val x2   = res3(2)
  x2.print

  "-----------------".print
  l.map(x => res2(x)).print // eq l.map(res2)
  l
    //.map(x => x.toString)
    .map(x => hof2(3).apply(x))
    .print // eq l.map(hof2(3))

  object ModifyLuitInt {
    def add2(ints: List[Int])    = ints.map(x => x + 2)
    def prodBy2(ints: List[Int]) = ints.map(_ * 2)
    def quad(ints: List[Int])    = ints.map(i => i * i)
  }
  object ModifyLuitIntV2 {
    private def applyFToList(ints: List[Int])(f: Int => Int): List[Int] = {
      val tmp = ints.map(f) // ints.map(x => f(x))
      println(tmp)
      tmp
    }
    def add2(ints: List[Int])    = applyFToList(ints)(i => i + 2)
    def prodBy2(ints: List[Int]) = applyFToList(ints)(i => i * 2)
    def quad(ints: List[Int])    = applyFToList(ints)(i => i * i)
  }

}
object ScalaCurry extends App with ImplicitForPrint {

  def sum(x: Int)(y: Int): Int = x + y

  def increment: Int => Int = sum(1)(_)
  increment.print
  increment(2).print

  val l = List(1, 2, 3)
  l.map(increment).print

  def sumF(f: Int => Int)(x: Int, y: Int): Int = f(x) + f(y)

  def justSum: (Int, Int) => Int = sumF(identity)(_, _)
  // x => x
  def justSumV2: (Int, Int) => Int = sumF(x => x)(_, _)

  val eqJusSum = justSum(1, 2) == justSumV2(1, 2)
  eqJusSum.print

  def sumSquare: (Int, Int) => Int = sumF(x => x * x)(_, _)
  sumSquare(2, 3).print // 13

  def incrementV2: Int => Int = justSum(1, _)
  incrementV2(2).print // 3

  object TransformNameByPhone extends App with ImplicitForPrint {
    val ln          = "Уланов"
    val fn          = "Павел"
    val mn          = "Олегович"
    val phoneExists = true

    def transformNameByPhoneExists(name: String, phoneExists: Boolean) =
      Option(name)
        .map(s => if (phoneExists) s.head.toString else s)
        .getOrElse("")
        .capitalize

    val curryTransformNameByPhoneExists = transformNameByPhoneExists(_, phoneExists)

    val cLn = Option(ln).getOrElse("").capitalize
    val cFn = curryTransformNameByPhoneExists(fn)
    val cMn = curryTransformNameByPhoneExists(mn)

    val tmp = List(fn, mn).map(curryTransformNameByPhoneExists).mkString(" ")        // П О
    tmp.print
    s"$cLn $tmp".print //Уланов П О
    s"$cLn $cFn $cMn".print                                                          //Уланов П О
  }

}
object ScalaPartialFunction extends App with ImplicitForPrint {

  def squareRoot: PartialFunction[Double, Double] = {
    case x if x >= 0 => Math.sqrt(x)
  }

  squareRoot.isDefinedAt(-1).print
//  squareRoot(-1)

  val l = List(1d, -2d, 3d)

  l.filter(x => squareRoot.isDefinedAt(x) && x % 2 == 0).map(squareRoot).print
  l.collect(squareRoot).print

  def negativeOrZer: PartialFunction[Int, Int] = {
    case x if x <= 0 => Math.abs(x)
  }
  def positeToNega: PartialFunction[Int, Int] = {
    case x if x > 0 => -1 * x
  }

  def swapSign: PartialFunction[Int, Int] = {
    negativeOrZer.orElse(positeToNega)
  }

  swapSign(-1).print
  swapSign(2).print

  "-------------".print

  def printIfPositive: PartialFunction[Int, Unit] = {
    case x if x > 0 => s"$x is pos!".print
  }

  def swapAndPint: PartialFunction[Int, Unit] = swapSign.andThen(printIfPositive)

  swapAndPint(-3)
  // swapAndPint(4) // MatchError
}

object ScalaImplicits extends App with ImplicitForPrint {

  // 1. implicit val
  // 2. implicit def
  // 3. implicit class

  case class ExampleForImpl(value: String)
  def printExmapleValue(implicit efi: ExampleForImpl) = efi.value.print

  implicit val efi1 = ExampleForImpl("test 1")

  printExmapleValue //test 1

  case class A(i: Int)
  case class B(i: Int)

  implicit def aToB(a: A) = B(a.i)

  val a    = A(1)
  val b: B = a

  implicit def strToInt(s: String): Int = s.length

  val x  = "sss" - 2 // eq strToInt( "sss" )  - 2
  val a2 = A("aaa") // eq A( strToInt( "aaa" ) )

  implicit class IntOps(i: Int) {
    def opt: Option[Int] = Option(i)
  }

  val x1: Option[Int] = 1.opt
  x1.print

  //Ctrl Alt Shift + хоткей для отображения имплистов
  //Ctrl Alt Shift - хоткей для сокрытия отображения имплистов
  //Ctrl Shift p хоткей для деталей имплиста

}
