package lec25_1.lecture

object ViewCollections extends App {
  val list = List(1, 2, 3, 4, 5)
  // map(_ + 3) из-за view
  // toList чтоб закрыть ленивую коллекцию и вычислить map`ы
  val x = list.view.map(_ + 1).map(_ + 1).filter(_ > 4).map(_ + 1).toList
  println(x)
}

object MutableCollections extends App {
  val il = /*scala.collection.immutable.*/ List(1, 2, 3, 4, 5)
  val ml = scala.collection.mutable.ListBuffer(1, 2, 3, 4, 5)
//  scala.collection.mutable.Seq(1, 2, 3, 4, 5)
//  scala.collection.mutable.ArrayBuffer(1, 2, 3, 4, 5)
//  scala.collection.mutable.Set(1, 2, 3, 4, 5)
  println(il)
  val il1 = il.prepended(1)
  println(il1)
  val il2 = il.appended(1)
  println(il2)
  println(il)
  println("--------------")
  println(ml)
  val ml1 = ml.addOne(1)
  println(ml1)
  val ml2 = ml.prepend(1)
  println(ml2)
  println(ml)
}

object VarWithCollections extends App {
  var lvar = List(1, 2, 3)
  val lval = List(1, 2, 3)
  lvar = lval //.map(_ + 1)
  // lval = lvar // Reassignment to val

  var mlvar = scala.collection.mutable.ListBuffer(1, 2, 3)
  val mlval = scala.collection.mutable.ListBuffer(1, 2, 3)
  println(mlvar)
  mlvar = mlval.addOne(1)
  println(mlvar)
  println(mlval)
  def withImmutableList(l: Iterable[Int]): List[Int] = List(1)
  withImmutableList(mlvar)
}

object MutableCollectionsWithDef extends App {
  import scala.collection.mutable.ListBuffer
  def mutate(lb: ListBuffer[Int]): Unit = lb.addOne(1)

  val lb = ListBuffer(1, 2, 3, 4, 5)
  mutate(lb)
  println(lb)
}

object MemoryJoker extends App {
  // вообразите что тут ДЖАВА класс со всеми вытекающими последствиями
  case class Cat(var name: String)
  val l = List(Cat("Urza"))
  val l1 = l.map { c =>
    // 10 code line
    c.name = "Murzik"
    // 15 code line
//    c.copy(name = "Murzik") // better way
    c
  }
  println(l)  // List(Cat(Murzik))
  println(l1) // List(Cat(Murzik))
}

object CompAndThenFunctions extends App {
  def addOne(i: Int): Int  = i + 1
  def prodTwo(i: Int): Int = i * 2
//  val addOne1: Int => Int = _ + 1

  // x = 2
  // addOne compose prodTwo 2
  // addOne(prodTwo(2))
  def comp = (addOne _).compose(prodTwo)
  println(comp(2))

  // x = 2
  // addOne 2 andThen prodTwo
  // prodTwo(addOne(2))
  def anThen = (addOne _).andThen(prodTwo)
  println(anThen(2))
}

object PartialFunctions extends App {
  def squareRoot: PartialFunction[Double, Double] = {
    case x if x >= 0 => Math.sqrt(x)
  }
  val x = -1
  if (squareRoot.isDefinedAt(x)) squareRoot(x) // check PartialFunction
  val res = squareRoot(4)
  println(res)

  val l = List(-1d, 4d).collect(squareRoot)
  println(l)

  val l1 = List(Option(1), Option(2), None)
  println(l1.flatten.filter(_ > 1))
  println {
    l1.collect {
      case Some(i) if i > 1 => i
    }
  }
}

object PartialFunctionsOrElse extends App {
  def negativeOrZeroToPos: PartialFunction[Int, Int] = {
    case x if x <= 0 => Math.abs(x)
  }
  def positiveToNegative: PartialFunction[Int, Int] = {
    case x if x > 0 => -x
  }
  def swapSign: PartialFunction[Int, Int] = {
    positiveToNegative.orElse(negativeOrZeroToPos)
  }
  println(swapSign(-1))
  println(swapSign(2))

  def printPos: PartialFunction[Int, Unit] = {
    case x if x > 0 => println(s"$x is positive")
  }
  def swapAndPrintIfPos: PartialFunction[Int, Unit] = swapSign
    .andThen(printPos)
    .orElse { case _ => }

  swapAndPrintIfPos(-4)
  swapAndPrintIfPos(4) // ce match without .orElse { case _ => }
}
