package lec25_1.lecture

object Lec10Hof extends App {
  // Higher Order Function
  def hof1(i: Int, f: Int => Int): Int = f(i)
  def quad(i: Int): Int                = i * i
  val res                              = hof1(2, quad)
  //  println(res)

  def hof2(i: Int): Int => Int = a => i * a

  val res2: Int => Int = hof2(2)
  //  println(res2)

  val l = List(1, 2, 3, 4, 5)
  println(l.map(res2))

  object ModifyListInt {
    def add2(ints: List[Int]): List[Int]  = ints.map(i => i + 2) // .map(_ + 2)
    def prod2(ints: List[Int]): List[Int] = ints.map(i => i * 2) // .map(_ * 2)
    def quad(ints: List[Int]): List[Int]  = ints.map(i => i * i)
  }

  object ModifyListIntV2 {
    private def applyFunctionToListInt(ints: List[Int], f: Int => Int): List[Int] = ints.map(i => f(i))
    def add2(ints: List[Int]): List[Int]                                          = applyFunctionToListInt(ints, i => i + 2) // .. (ints, _ + 2)
    def prod2(ints: List[Int]): List[Int]                                         = applyFunctionToListInt(ints, i => i * 2) // .. (ints, _ * 2)
    def quad(ints: List[Int]): List[Int]                                          = applyFunctionToListInt(ints, i => i * i)
  }

  val l1 = List(1, 2, 3, 4, 5)
  println(s"addV1: ${ModifyListInt.add2(l1)}")
  println(s"prdoV1: ${ModifyListInt.prod2(l1)}")
  println(s"quadV1: ${ModifyListInt.quad(l1)}")
  println("-------------")
  println(s"addV2: ${ModifyListIntV2.add2(l1)}")
  println(s"prdoV2: ${ModifyListIntV2.prod2(l1)}")
  println(s"quadV2: ${ModifyListIntV2.quad(l1)}")
}

object Lec10Currying extends App {
  def sum(x: Int)(y: Int) = x.toDouble / y.toDouble
  def increment           = sum(1)(_)

  val twoIncOne = increment(0)
  println(twoIncOne)

  val l1 = List(1, 2, 3, 4, 5)
  println(l1.map(x => increment(x)))
  println(l1.map(increment))
  def summF(f: Int => Int)(x: Int, y: Int): Int = f(x) + f(y)
  def justSum                                   = summF(identity)(_, _)
  def justSum2                                  = summF(x => x)(_, _)
  val eqJustSum                                 = justSum(1, 2) == justSum2(1, 2)
  println(eqJustSum)

  def squareSum = summF(x => x * x)(_, _)
  println(squareSum(2, 3))

  def incrementV2 = justSum(1, _)
  println(incrementV2(4))

  class A(f: => Int) {
    def print(): Int = {
      f
      f
      f
    }
  }

  //  val x = {
  //    println(System.currentTimeMillis())
  //    Thread.sleep(10000)
  //    1
  //  }

  var y = 0
  lazy val x = {
    //    println(System.currentTimeMillis())
    //    Thread.sleep(5000)
    y = y + 1
    y
  }
  val c = new A(x)
  c.print()
  println(y)

  println("------------------")
  var z = 0
  val b = new A({
    //    println(System.currentTimeMillis())
    //    Thread.sleep(5000)
    z = z + 1
    z
  })
  b.print()
  println(z)

  //  println(null == null) // true
  //  println(null eq null) // true
  // println(null equals  null) NPE!!!!
}
