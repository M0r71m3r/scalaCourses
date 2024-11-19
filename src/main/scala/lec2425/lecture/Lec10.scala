package lec2425.lecture

object MutableCollections extends App {
  val immutableList = List(1, 2, 3, 4)
  // view => lazy collection,
  // in the end .toList for strict collection and compute result
  // via view => map(_ + 1).map(_ + 1).map(_ + 1) => "map(_ + 3)"
  val immutableList1: List[Int] =
    immutableList.view.map(_ + 1).map(_ + 1).map(_ + 1).toList
  println(immutableList)  // 1, 2, 3, 4
  println(immutableList1) // 4, 5, 6, 7
  println("-------------------")
  val mutableList = scala.collection.mutable.ListBuffer(1, 2, 3, 4)
  println(mutableList)
  val mutableList1 = mutableList.prepend(1)
  println(mutableList1)
  println(mutableList)
}

object VarWithCollections extends App {
  var immutableList = List(1, 2, 3, 4)
  println(immutableList) // List(1, 2, 3, 4)
  // set copy of immutableList
  immutableList = immutableList.map(_ + 1)
  println(immutableList) // List(2, 3, 4, 5)
  println("----------")
  val mutableList = scala.collection.mutable.ListBuffer(1, 2, 3, 4)
  println(mutableList.map(_ + 1)) // ListBuffer(2, 3, 4, 5)
  println(mutableList)            // ListBuffer(1, 2, 3, 4)
}

object MutableCollectionsWithDef extends App {
  def mutateCol(
      mc: scala.collection.mutable.ListBuffer[Int]
  ): scala.collection.mutable.ListBuffer[Int] = {
    mc.prepend(1)
  }
  val mutableList = scala.collection.mutable.ListBuffer(1, 2, 3, 4)
  println(mutableList)
  // more code
  println(mutateCol(mutableList))
  // more code
  println(mutableList)
}

object CompAndThenFunctions extends App {
  // def to val => addOne _
  val addOne: Int => Int  = _ + 1
  val prodTwo: Int => Int = _ * 2

  val i = 1

  val mi1 = prodTwo(addOne(i))
  println(mi1)
  // i = 1
  // addOne 1 andThen prodTwo
  // prodTwo(addOne(i))
  // addOne >> prodTwo >> prodTwo >> prodTwo
  val at: Int => Int = addOne.andThen(prodTwo).andThen(prodTwo).andThen(prodTwo)
  val mi1_1          = at(i)
  println(mi1_1)

  val mi2 = addOne(prodTwo(i))
  println(mi2)
  // i = 1
  // addOne compose prodTwo 1
  // addOne(prodTwo(i))
  val comp: Int => Int = addOne.compose(prodTwo)
  val mi2_1            = comp(i)
  println(mi2_1)

  val f1: Int => Int = (i: Int) => addOne(prodTwo(prodTwo(prodTwo(i))))
  // Int => MyObject
  // Int => MyObject1 => MyObject2 => MyObject
}

object PartialFunctions extends App {
  def sqrtRoot: PartialFunction[Double, Double] = {
    case x if x >= 0 => Math.sqrt(x)
  }
  def sqrtRootBad: PartialFunction[Double, Double] = {
    case x if x >= 0 => Math.sqrt(x)
    case _           => -1
  }

  println(Math.sqrt(-1))

  val b = sqrtRoot.isDefinedAt(-1)
//  println(b)

  def applyDouble(d: Double) = {
    if (sqrtRoot.isDefinedAt(d)) sqrtRoot(d) else -1
  }

//  println(applyDouble(-2))
//  println(applyDouble(4))

  // println(sqrtRoot(-1)) // scala.MatchError

  val list = List(-1.0, 2.0, -3.0, 5.0, -6.0)
  println(list.collect(sqrtRoot))
  println(list.collect(sqrtRootBad))

}

object PartialFunctionsOrElse extends App {
  def negativeOrZeroToPos: PartialFunction[Int, Int] = {
    case x if x <= 0 => Math.abs(x)
  }
  def posToNeg: PartialFunction[Int, Int] = {
    case x if x > 0 => -1 * x
  }
  def swapSign: PartialFunction[Int, Int] = {
    posToNeg.orElse(negativeOrZeroToPos)
  }
  println(swapSign(-1))
  println(swapSign(2))

  def printIfPos: PartialFunction[Int, Unit] = {
    case x if x > 0 => println(s"x > 0 : $x")
  }
  def swapSignAndPrintIfPos: PartialFunction[Int, Unit] =
    swapSign.andThen(printIfPos)

  swapSignAndPrintIfPos(-4)
  swapSignAndPrintIfPos(4)
}
